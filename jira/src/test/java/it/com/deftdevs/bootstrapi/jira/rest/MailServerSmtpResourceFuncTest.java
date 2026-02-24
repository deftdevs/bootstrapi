package it.com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import it.com.deftdevs.bootstrapi.commons.rest.AbstractMailServerSmtpResourceFuncTest;

public class MailServerSmtpResourceFuncTest extends AbstractMailServerSmtpResourceFuncTest {

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
