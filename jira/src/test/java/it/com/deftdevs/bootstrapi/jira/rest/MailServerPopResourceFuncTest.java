package it.com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.model.MailServerPopModel;
import it.com.deftdevs.bootstrapi.commons.rest.AbstractMailServerPopResourceFuncTest;

public class MailServerPopResourceFuncTest extends AbstractMailServerPopResourceFuncTest {

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
