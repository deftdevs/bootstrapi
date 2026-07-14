package com.deftdevs.bootstrapi.crowd.cli;

import com.deftdevs.bootstrapi.commons.cli.SetupEnv;
import com.deftdevs.bootstrapi.commons.cli.SetupException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Sets up a pristine Crowd from the official container image by driving the
 * complete setup wizard, backed by a PostgreSQL container. Gated behind
 * {@code BOOTSTRAPI_SETUP_IT=true} because it downloads and boots real
 * product containers.
 * <p>
 * Without a license in {@code BOOTSTRAPI_SETUP_IT_LICENSE} only the wizard
 * mechanics are verified: the CLI must walk the wizard up to the license
 * validation and fail loud, not silently.
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
            .withDatabaseName("crowd")
            .withUsername("crowd")
            .withPassword("crowd");

    @Container
    private static final GenericContainer<?> CROWD = new GenericContainer<>(
            SetupEnv.get("BOOTSTRAPI_SETUP_IT_IMAGE", "atlassian/crowd:7.2.1"))
            .withNetwork(NETWORK)
            .withExposedPorts(8095);

    @AfterAll
    static void teardown() {
        System.getProperties().keySet().removeIf(key -> key.toString().startsWith("BOOTSTRAPI_SETUP_"));
    }

    @Test
    void testSetup() {
        final String license = SetupEnv.get("BOOTSTRAPI_SETUP_IT_LICENSE", null);
        final String baseUrl = "http://" + CROWD.getHost() + ":" + CROWD.getMappedPort(8095) + "/crowd";

        System.setProperty("BOOTSTRAPI_SETUP_BASE_URL", baseUrl);
        System.setProperty("BOOTSTRAPI_SETUP_TIMEOUT_SECONDS", "600");
        System.setProperty("BOOTSTRAPI_SETUP_LICENSE", license != null ? license : "INVALID-LICENSE-KEY");
        System.setProperty("BOOTSTRAPI_SETUP_TITLE", "Setup IT");
        System.setProperty("BOOTSTRAPI_SETUP_DATABASE_TYPE", "PostgreSQL");
        System.setProperty("BOOTSTRAPI_SETUP_DATABASE_DRIVER", "org.postgresql.Driver");
        System.setProperty("BOOTSTRAPI_SETUP_DATABASE_URL", "jdbc:postgresql://postgres:5432/crowd");
        System.setProperty("BOOTSTRAPI_SETUP_DATABASE_USERNAME", "crowd");
        System.setProperty("BOOTSTRAPI_SETUP_DATABASE_PASSWORD", "crowd");
        System.setProperty("BOOTSTRAPI_SETUP_DATABASE_DIALECT", "org.hibernate.dialect.PostgreSQLDialect");
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_USERNAME", "admin");
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_PASSWORD", "admin-secret-1");
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_EMAIL", "admin@example.com");

        if (license == null) {
            // without a real license the wizard silently stays behind (it answers
            // invalid input with an error page, not an error status), so the only
            // acceptable outcome is that the CLI fails loud at whatever step the
            // rejection surfaces
            assertThrows(SetupException.class, () -> new SetupCli(new SetupHttpSession(baseUrl)).run());
            return;
        }

        assertEquals(0, new SetupCli(new SetupHttpSession(baseUrl)).run());

        // the wizard is gone and a second run is a no-op
        final SetupHttpSession session = new SetupHttpSession(baseUrl);
        assumeTrue(session.getLocation("/console/login.action").isEmpty());
        assertEquals(0, new SetupCli(session).run());
    }
}
