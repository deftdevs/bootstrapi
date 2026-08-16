package com.deftdevs.bootstrapi.confluence.cli;

import com.deftdevs.bootstrapi.commons.cli.SetupEnv;
import com.deftdevs.bootstrapi.commons.cli.SetupException;
import com.deftdevs.bootstrapi.commons.cli.SetupHttpSession;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Drives the Confluence setup wizard over HTTP so a fresh instance can be
 * set up unattended, e.g. from a deployment hook job. Run it directly from
 * the plugin JAR: {@code java -jar bootstrapi-confluence-plugin.jar}.
 * <p>
 * The license and database connection must already be configured (a
 * pre-seeded {@code confluence.cfg.xml}); the wizard then starts at the
 * data step.
 */
public class SetupCli {

    private final SetupHttpSession session;

    // the setup steps in wizard order with the redirect markers used to resume
    private final List<Step> steps = List.of(
            new Step("/setup/setupcluster-start.action", this::setupCluster),
            new Step("/setup/setupdata-start.action", this::setupData),
            new Step("/setup/setupusermanagementchoice-start.action", this::setupUserManagement),
            new Step("/setup/setupadministrator-start.action", this::setupAdministrator),
            new Step("/setup/finishsetup-start.action", this::finishSetup));

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

        // the status endpoint answers with STARTING long before the application is
        // ready; FIRST_RUN means the setup wizard is pending
        final String status = session.waitForAnyState("/status", timeout, pollInterval, "RUNNING", "FIRST_RUN");
        if (status.contains("RUNNING")) {
            System.out.println("Confluence is already set up, nothing to do.");
            return 0;
        }

        // the initial page request creates the session cookie
        session.get("/");

        final String stepLocation = session.getLocation("/bootstrap/selectsetupstep.action")
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
        if (!session.get("/status").contains("RUNNING")) {
            throw new SetupException("The setup did not complete; the application does not report state RUNNING"
                    + " (most likely a submitted value was rejected)");
        }

        System.out.println("Setting up Confluence done.");
        return 0;
    }

    private void setupCluster() {
        System.out.println("Skipping the clustering setup...");

        // a cluster is configured through confluence.cfg.xml, not through the wizard

        final Map<String, String> form = new LinkedHashMap<>();
        form.put("newCluster", "skipCluster");
        form.put("atl_token", token());
        session.postForm("/setup/setupcluster.action", form);
    }

    private void setupData() {
        System.out.println("Setting up data...");
        final Map<String, String> form = new LinkedHashMap<>();
        form.put("dbchoiceSelect", "Empty Site");
        form.put("contentChoice", "blank");
        form.put("atl_token", token());
        session.postForm("/setup/setupdata.action", form);
    }

    private void setupUserManagement() {
        System.out.println("Setting up user management...");
        final Map<String, String> form = new LinkedHashMap<>();
        form.put("userManagementChoice", "internal");
        form.put("internal", "Manage users and groups within Confluence");
        form.put("atl_token", token());
        session.postForm("/setup/setupusermanagementchoice.action", form);
    }

    private void setupAdministrator() {
        System.out.println("Setting up administrator...");
        final String password = SetupEnv.require("BOOTSTRAPI_SETUP_ADMIN_PASSWORD");

        final Map<String, String> form = new LinkedHashMap<>();
        form.put("username", SetupEnv.require("BOOTSTRAPI_SETUP_ADMIN_USERNAME"));
        form.put("fullName", SetupEnv.require("BOOTSTRAPI_SETUP_ADMIN_FULL_NAME"));
        form.put("email", SetupEnv.require("BOOTSTRAPI_SETUP_ADMIN_EMAIL"));
        form.put("password", password);
        form.put("confirm", password);
        form.put("setup-next-button", "Next");
        form.put("atl_token", token());
        session.postForm("/setup/setupadministrator.action", form);
    }

    private void finishSetup() {
        System.out.println("Finishing the setup...");
        session.get("/setup/finishsetup.action");
    }

    private String token() {
        return SetupHttpSession.parseFormInput(session.get("/"), "atl_token");
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
