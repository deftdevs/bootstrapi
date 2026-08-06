package com.deftdevs.bootstrapi.jira.cli;

import com.deftdevs.bootstrapi.commons.cli.SetupEnv;
import com.deftdevs.bootstrapi.commons.cli.SetupException;
import com.deftdevs.bootstrapi.commons.cli.SetupHttpSession;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Drives the Jira setup wizard over HTTP so a fresh instance can be set up
 * unattended, e.g. from a deployment hook job. Run it directly from the
 * plugin JAR: {@code java -jar bootstrapi-jira-plugin.jar}.
 * <p>
 * On an empty database the wizard starts with the database step, which is
 * driven from the {@code BOOTSTRAPI_SETUP_DATABASE_*} variables; afterwards
 * (or when the database is already initialised) the wizard continues at the
 * application properties step.
 */
public class SetupCli {

    private final SetupHttpSession session;

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
            System.out.println("Jira is already set up, nothing to do.");
            return 0;
        }

        // the initial page request creates the session and shows the current setup step
        String page = session.get("/");
        if (page.contains("SetupDatabase.jspa")) {
            setupDatabase(page);
            page = session.get("/");
        }

        String token = SetupHttpSession.parseFormInput(page, "atl_token");
        token = setupApplicationProperties(token);
        token = setupLicense(token);
        token = setupAdministrator(token);
        setupMailNotifications(token);

        // the wizard answers invalid input with a 200 error page and stays on the
        // current step, so only the resulting state proves the setup went through
        if (!session.get("/status").contains("RUNNING")) {
            throw new SetupException("The setup did not complete; the application does not report state RUNNING"
                    + " (most likely a submitted value was rejected, e.g. an invalid license)");
        }

        System.out.println("Setting up Jira done.");
        return 0;
    }

    private void setupDatabase(
            final String page) {

        System.out.println("Setting up the database (this initialises the database and can take a while)...");
        final Map<String, String> form = new LinkedHashMap<>();
        form.put("atl_token", SetupHttpSession.parseFormInput(page, "atl_token"));
        form.put("databaseOption", "external");
        form.put("databaseType", SetupEnv.get("BOOTSTRAPI_SETUP_DATABASE_TYPE", "postgres72"));
        form.put("jdbcHostname", SetupEnv.require("BOOTSTRAPI_SETUP_DATABASE_HOSTNAME"));
        form.put("jdbcPort", SetupEnv.get("BOOTSTRAPI_SETUP_DATABASE_PORT", "5432"));
        form.put("jdbcDatabase", SetupEnv.require("BOOTSTRAPI_SETUP_DATABASE_NAME"));
        form.put("jdbcUsername", SetupEnv.require("BOOTSTRAPI_SETUP_DATABASE_USERNAME"));
        form.put("jdbcPassword", SetupEnv.require("BOOTSTRAPI_SETUP_DATABASE_PASSWORD"));
        form.put("schemaName", SetupEnv.get("BOOTSTRAPI_SETUP_DATABASE_SCHEMA", "public"));
        form.put("testingConnection", "false");
        session.postForm("/secure/SetupDatabase.jspa", form);
    }

    private String setupApplicationProperties(
            final String token) {

        System.out.println("Setting up application properties...");
        final Map<String, String> form = new LinkedHashMap<>();
        form.put("baseURL", SetupEnv.require("BOOTSTRAPI_SETUP_BASE_URL"));
        form.put("title", SetupEnv.require("BOOTSTRAPI_SETUP_TITLE"));
        form.put("mode", SetupEnv.get("BOOTSTRAPI_SETUP_MODE", "private"));
        form.put("nextStep", "true");
        form.put("atl_token", token);
        return SetupHttpSession.parseFormInput(
                session.postForm("/secure/SetupApplicationProperties.jspa", form), "atl_token");
    }

    private String setupLicense(
            final String token) {

        System.out.println("Setting up license (this continues initialising the database and can take a while)...");
        final int maxRetries = Integer.parseInt(SetupEnv.get("BOOTSTRAPI_SETUP_MAX_RETRIES", "3"));
        final long retryDelayMillis = Long.parseLong(SetupEnv.get("BOOTSTRAPI_SETUP_RETRY_SECONDS", "10")) * 1000;

        final Map<String, String> form = new LinkedHashMap<>();
        form.put("setupLicenseKey", SetupEnv.require("BOOTSTRAPI_SETUP_LICENSE"));
        form.put("atl_token", token);

        // freshly started instances occasionally answer with an error; retry a few times
        for (int attempt = 0; ; attempt++) {
            try {
                return SetupHttpSession.parseFormInput(session.postForm("/secure/SetupLicense.jspa", form), "atl_token");
            } catch (SetupException e) {
                if (attempt >= maxRetries) {
                    throw e;
                }
                System.out.println("Setting up the license failed, retrying: " + e.getMessage());
                try {
                    Thread.sleep(retryDelayMillis);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new SetupException("Interrupted while retrying the license setup", ie);
                }
            }
        }
    }

    private String setupAdministrator(
            final String token) {

        System.out.println("Setting up administrator...");
        final String password = SetupEnv.require("BOOTSTRAPI_SETUP_ADMIN_PASSWORD");

        final Map<String, String> form = new LinkedHashMap<>();
        form.put("fullname", SetupEnv.require("BOOTSTRAPI_SETUP_ADMIN_FULL_NAME"));
        form.put("email", SetupEnv.require("BOOTSTRAPI_SETUP_ADMIN_EMAIL"));
        form.put("username", SetupEnv.require("BOOTSTRAPI_SETUP_ADMIN_USERNAME"));
        form.put("password", password);
        form.put("confirm", password);
        form.put("atl_token", token);
        return SetupHttpSession.parseFormInput(session.postForm("/secure/SetupAdminAccount.jspa", form), "atl_token");
    }

    private void setupMailNotifications(
            final String token) {

        System.out.println("Finishing the setup (skipping mail notifications)...");

        // the mail server is skipped here; it can be configured through the REST API

        final Map<String, String> form = new LinkedHashMap<>();
        form.put("noemail", "true");
        form.put("atl_token", token);
        session.postForm("/secure/SetupMailNotifications.jspa", form);
    }
}
