package it.com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import it.com.deftdevs.bootstrapi.commons.rest.AbstractMailServerSmtpResourceFuncTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class MailServerSmtpResourceFuncTest extends AbstractMailServerSmtpResourceFuncTest {

    @Test
    @Order(1)
    @Override
    protected void testGetMailServerSmtpNotConfigured() {
        // no-op: Confluence's test instance already has SMTP mail configuration
    }

    @Override
    protected MailServerSmtpModel getExampleModel() {
        return MailServerSmtpModel.builder()
                .name("Test SMTP Server")
                .from("test@example.com")
                .prefix("[Test]")
                .protocol("smtp")
                .host("localhost")
                .port(3025)
                .useTls(false)
                .timeout(5000L)
                .build();
    }
}
