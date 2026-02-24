package it.com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import it.com.deftdevs.bootstrapi.commons.rest.AbstractMailServerSmtpResourceFuncTest;

public class MailServerSmtpResourceFuncTest extends AbstractMailServerSmtpResourceFuncTest {

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
