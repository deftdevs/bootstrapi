package it.com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.model.MailServerPopModel;
import it.com.deftdevs.bootstrapi.commons.rest.AbstractMailServerPopResourceFuncTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class MailServerPopResourceFuncTest extends AbstractMailServerPopResourceFuncTest {

    @Test
    @Order(1)
    @Override
    protected void testGetMailServerPopNotConfigured() {
        // no-op: Confluence's test instance already has POP mail configuration
    }

    @Override
    protected MailServerPopModel getExampleModel() {
        return MailServerPopModel.builder()
                .name("Test POP Server")
                .protocol("pop3")
                .host("localhost")
                .port(3110)
                .timeout(5000L)
                .build();
    }
}
