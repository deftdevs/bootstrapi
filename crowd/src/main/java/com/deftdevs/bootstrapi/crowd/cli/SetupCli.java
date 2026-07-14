package com.deftdevs.bootstrapi.crowd.cli;

import com.deftdevs.bootstrapi.commons.cli.SetupEnv;
import com.deftdevs.bootstrapi.commons.cli.SetupException;
import com.deftdevs.bootstrapi.commons.cli.SetupHttpSession;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Drives the Crowd setup wizard over HTTP so a fresh instance can be set up
 * unattended, e.g. from a deployment hook job. Run it directly from the
 * plugin JAR: {@code java -jar bootstrapi-crowd-plugin.jar}.
 * <p>
 * The wizard flow is based on the crowd-init script of the ldap-crowd-adapter
 * project (Apache License 2.0, ASERVO Software GmbH):
 * https://github.com/aservo/ldap-crowd-adapter
 */
public class SetupCli {

    private final SetupHttpSession session;

    // the setup steps in wizard order with the redirect markers used to resume
    private final List<Step> steps = List.of(
            new Step("/console/setup/setuplicense.action", this::setupLicense),
            new Step("/console/setup/installtype.action", this::setupInstallType),
            new Step("/console/setup/setupdatabase.action", this::setupDatabase),
            new Step("/console/setup/setupoptions.action", this::setupOptions),
            new Step("/console/setup/directoryinternalsetup.action", this::setupInternalDirectory),
            new Step("/console/setup/defaultadministrator.action", this::setupAdministrator),
            new Step("/console/setup/integration.action", this::setupIntegration));

    public static void main(
            final String[] args) {

        try {
            System.exit(new SetupCli(new SetupHttpSession(SetupEnv.require("BOOTSTRAPI_SETUP_BASE_URL"))).run());
        } catch (SetupException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    SetupCli(
            final SetupHttpSession session) {

        this.session = session;
    }

    int run() {
        final Duration timeout = Duration.ofSeconds(Long.parseLong(SetupEnv.get("BOOTSTRAPI_SETUP_TIMEOUT_SECONDS", "300")));
        final Duration pollInterval = Duration.ofSeconds(Long.parseLong(SetupEnv.get("BOOTSTRAPI_SETUP_POLL_SECONDS", "5")));
        session.waitUntilAvailable("/", timeout, pollInterval);

        final Optional<String> loginLocation = session.getLocation("/console/login.action");
        if (loginLocation.isEmpty() || !loginLocation.get().contains("/console/setup/")) {
            System.out.println("Crowd is already set up, nothing to do.");
            return 0;
        }

        final String stepLocation = session.getLocation("/console/setup/selectsetupstep.action")
                .orElseThrow(() -> new SetupException("The setup step selection did not redirect to a setup step"));

        int stepIndex = -1;
        for (int i = 0; i < steps.size(); i++) {
            if (stepLocation.contains(steps.get(i).marker)) {
                stepIndex = i;
                break;
            }
        }
        if (stepIndex < 0) {
            throw new SetupException("Unknown setup step: " + stepLocation);
        }

        for (int i = stepIndex; i < steps.size(); i++) {
            steps.get(i).action.run();
        }

        // the wizard answers invalid input with a 200 error page and stays on the
        // current step, so only the resulting state proves the setup went through
        final Optional<String> verification = session.getLocation("/console/login.action");
        if (verification.isPresent() && verification.get().contains("/console/setup/")) {
            throw new SetupException("The setup did not complete; the wizard is still at " + verification.get()
                    + " (most likely a submitted value was rejected, e.g. an invalid license)");
        }

        System.out.println("Setting up Crowd done.");
        return 0;
    }

    private void setupLicense() {
        System.out.println("Setting up license...");
        final String page = session.get("/console/setup/setuplicense.action");
        final String serverId = SetupEnv.get("BOOTSTRAPI_SETUP_SERVER_ID",
                SetupHttpSession.parseFormInput(page, "sid"));

        final Map<String, String> form = new LinkedHashMap<>();
        form.put("atl_token", SetupHttpSession.parseFormInput(page, "atl_token"));
        form.put("sid", serverId);
        form.put("key", SetupEnv.require("BOOTSTRAPI_SETUP_LICENSE"));
        session.postForm("/console/setup/setuplicense!update.action", form);
    }

    private void setupInstallType() {
        System.out.println("Setting up install type...");
        final Map<String, String> form = new LinkedHashMap<>();
        form.put("atl_token", stepToken("/console/setup/installtype.action"));
        form.put("installOption", "install.new");
        session.postForm("/console/setup/installtype!update.action", form);
    }

    private void setupDatabase() {
        System.out.println("Setting up database...");
        final Map<String, String> form = new LinkedHashMap<>();
        form.put("atl_token", stepToken("/console/setup/setupdatabase.action"));

        if ("embedded".equals(SetupEnv.get("BOOTSTRAPI_SETUP_DATABASE_OPTION", "jdbc"))) {
            form.put("databaseOption", "db.embedded");
        } else {
            form.put("databaseOption", "db.jdbc");
            form.put("jdbcDatabaseType", SetupEnv.require("BOOTSTRAPI_SETUP_DATABASE_TYPE"));
            form.put("jdbcDriverClassName", SetupEnv.require("BOOTSTRAPI_SETUP_DATABASE_DRIVER"));
            form.put("jdbcUrl", SetupEnv.require("BOOTSTRAPI_SETUP_DATABASE_URL"));
            form.put("jdbcUsername", SetupEnv.require("BOOTSTRAPI_SETUP_DATABASE_USERNAME"));
            form.put("jdbcPassword", SetupEnv.require("BOOTSTRAPI_SETUP_DATABASE_PASSWORD"));
            form.put("jdbcHibernateDialect", SetupEnv.require("BOOTSTRAPI_SETUP_DATABASE_DIALECT"));
        }
        session.postForm("/console/setup/setupdatabase!update.action", form);
    }

    private void setupOptions() {
        System.out.println("Setting up options...");
        final Map<String, String> form = new LinkedHashMap<>();
        form.put("atl_token", stepToken("/console/setup/setupoptions.action"));
        form.put("title", SetupEnv.require("BOOTSTRAPI_SETUP_TITLE"));
        form.put("sessionTime", SetupEnv.get("BOOTSTRAPI_SETUP_SESSION_TIME", "30"));
        form.put("baseURL", SetupEnv.require("BOOTSTRAPI_SETUP_BASE_URL"));
        session.postForm("/console/setup/setupoptions!update.action", form);
    }

    private void setupInternalDirectory() {
        System.out.println("Setting up internal directory...");

        // the password policy is deliberately left open so setting up the administrator
        // cannot fail; policies can be applied afterwards through the REST API

        final Map<String, String> form = new LinkedHashMap<>();
        form.put("atl_token", stepToken("/console/setup/directoryinternalsetup.action"));
        form.put("name", SetupEnv.get("BOOTSTRAPI_SETUP_DIRECTORY_NAME", "Internal directory"));
        form.put("description", SetupEnv.get("BOOTSTRAPI_SETUP_DIRECTORY_DESCRIPTION", ""));
        form.put("passwordRegex", "");
        form.put("passwordComplexityMessage", "");
        form.put("passwordMaxAttempts", SetupEnv.get("BOOTSTRAPI_SETUP_DIRECTORY_PASSWORD_MAX_ATTEMPTS", "0"));
        form.put("passwordHistoryCount", SetupEnv.get("BOOTSTRAPI_SETUP_DIRECTORY_PASSWORD_HISTORY_COUNT", "0"));
        form.put("passwordMaxChangeTime", SetupEnv.get("BOOTSTRAPI_SETUP_DIRECTORY_PASSWORD_MAX_CHANGE_TIME", "0"));
        form.put("userEncryptionMethod", SetupEnv.get("BOOTSTRAPI_SETUP_DIRECTORY_PASSWORD_ENCRYPTION_METHOD", "atlassian-security"));
        session.postForm("/console/setup/directoryinternalsetup!update.action", form);
    }

    private void setupAdministrator() {
        System.out.println("Setting up administrator...");
        final String password = SetupEnv.require("BOOTSTRAPI_SETUP_ADMIN_PASSWORD");

        final Map<String, String> form = new LinkedHashMap<>();
        form.put("atl_token", stepToken("/console/setup/defaultadministrator.action"));
        form.put("email", SetupEnv.require("BOOTSTRAPI_SETUP_ADMIN_EMAIL"));
        form.put("name", SetupEnv.require("BOOTSTRAPI_SETUP_ADMIN_USERNAME"));
        form.put("firstname", SetupEnv.get("BOOTSTRAPI_SETUP_ADMIN_FIRST_NAME", "Admin"));
        form.put("lastname", SetupEnv.get("BOOTSTRAPI_SETUP_ADMIN_LAST_NAME", "Admin"));
        form.put("password", password);
        form.put("passwordConfirm", password);
        session.postForm("/console/setup/defaultadministrator!update.action", form);
    }

    private void setupIntegration() {
        System.out.println("Setting up integration...");

        // the only available integration is the OpenID server, which stays disabled;
        // newer Crowd versions no longer have this step and finish after the administrator
        final String token;
        try {
            token = stepToken("/console/setup/integration.action");
        } catch (SetupException e) {
            System.out.println("The integration step is not present, skipping.");
            return;
        }

        final Map<String, String> form = new LinkedHashMap<>();
        form.put("atl_token", token);
        session.postForm("/console/setup/integration!update.action", form);
    }

    private String stepToken(
            final String stepPath) {

        return SetupHttpSession.parseFormInput(session.get(stepPath), "atl_token");
    }

    private static class Step {

        private final String marker;
        private final Runnable action;

        private Step(
                final String marker,
                final Runnable action) {

            this.marker = marker;
            this.action = action;
        }
    }
}
