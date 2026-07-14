package com.deftdevs.bootstrapi.jira.cli;

import com.deftdevs.bootstrapi.commons.cli.SetupEnv;
import com.deftdevs.bootstrapi.commons.cli.SetupHttpSession;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Sets up a pristine Jira from the official container image by driving the
 * setup wizard, backed by a PostgreSQL container whose connection the image
 * templates into {@code dbconfig.xml}. Gated behind
 * {@code BOOTSTRAPI_SETUP_IT=true} and a license in
 * {@code BOOTSTRAPI_SETUP_IT_LICENSE} (the public Jira Data Center timebomb
 * license works).
 */
@Testcontainers
@EnabledIfEnvironmentVariable(named = "BOOTSTRAPI_SETUP_IT", matches = "true")
class SetupCliIT {

    static {
        // the bundled docker-java client defaults to API version 1.32, which recent
        // Docker daemons reject (Docker 29 requires at least 1.40); 1.41 is accepted
        // by every daemon since Docker 20.10
        if (System.getProperty("api.version") == null && System.getenv("DOCKER_API_VERSION") == null) {
            System.setProperty("api.version", "1.41");
        }
    }

    private static final Network NETWORK = Network.newNetwork();

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
            .withNetwork(NETWORK)
            .withNetworkAliases("postgres")
            .withDatabaseName("jira")
            .withUsername("jira")
            .withPassword("jira");

    @Container
    private static final GenericContainer<?> JIRA = new GenericContainer<>(
            SetupEnv.get("BOOTSTRAPI_SETUP_IT_IMAGE", "atlassian/jira-software:11.3.8"))
            .withNetwork(NETWORK)
            .withExposedPorts(8080)
            .withEnv("ATL_DB_TYPE", "postgres72")
            .withEnv("ATL_JDBC_URL", "jdbc:postgresql://postgres:5432/jira")
            .withEnv("ATL_JDBC_USER", "jira")
            .withEnv("ATL_JDBC_PASSWORD", "jira")
            .withEnv("JVM_MINIMUM_MEMORY", "1g")
            .withEnv("JVM_MAXIMUM_MEMORY", "2g");

    @AfterAll
    static void teardown() {
        System.getProperties().keySet().removeIf(key -> key.toString().startsWith("BOOTSTRAPI_SETUP_"));
    }

    @Test
    void testSetup() {
        final String license = SetupEnv.get("BOOTSTRAPI_SETUP_IT_LICENSE", null);
        assumeTrue(license != null, "BOOTSTRAPI_SETUP_IT_LICENSE is not set");

        final String baseUrl = "http://" + JIRA.getHost() + ":" + JIRA.getMappedPort(8080);
        System.setProperty("BOOTSTRAPI_SETUP_BASE_URL", baseUrl);
        System.setProperty("BOOTSTRAPI_SETUP_TIMEOUT_SECONDS", "900");
        System.setProperty("BOOTSTRAPI_SETUP_DATABASE_HOSTNAME", "postgres");
        System.setProperty("BOOTSTRAPI_SETUP_DATABASE_NAME", "jira");
        System.setProperty("BOOTSTRAPI_SETUP_DATABASE_USERNAME", "jira");
        System.setProperty("BOOTSTRAPI_SETUP_DATABASE_PASSWORD", "jira");
        System.setProperty("BOOTSTRAPI_SETUP_TITLE", "Setup IT");
        System.setProperty("BOOTSTRAPI_SETUP_LICENSE", license);
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_FULL_NAME", "Admin Admin");
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_EMAIL", "admin@example.com");
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_USERNAME", "admin");
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_PASSWORD", "admin-secret-1");

        final SetupHttpSession session = new SetupHttpSession(baseUrl);
        assertEquals(0, new SetupCli(session).run());

        assertTrue(session.get("/status").contains("RUNNING"));

        // a second run is a no-op
        assertEquals(0, new SetupCli(session).run());
    }
}
