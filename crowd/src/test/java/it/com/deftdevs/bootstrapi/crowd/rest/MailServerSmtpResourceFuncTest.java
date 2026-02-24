package it.com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import it.com.deftdevs.bootstrapi.commons.rest.AbstractMailServerSmtpResourceFuncTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class MailServerSmtpResourceFuncTest extends AbstractMailServerSmtpResourceFuncTest {

    // Crowd test data already has mail configured, so skip the "not configured" test
    @Test
    @Order(1)
    @Override
    protected void testGetMailServerSmtpNotConfigured() {
        // no-op: Crowd's test data already has mail configuration
    }

    @Override
    protected MailServerSmtpModel getExampleModel() {
        return MailServerSmtpModel.builder()
                .from("test@example.com")
                .prefix("[Test]")
                .host("localhost")
                .port(3025)
                .useTls(false)
                .timeout(5000L)
                .build();
    }
}
