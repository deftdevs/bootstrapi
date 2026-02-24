package com.atlassian.mail.server;

import com.atlassian.confluence.jmx.JmxSMTPMailServer;

public class OtherTestSmtpMailServerImpl extends JmxSMTPMailServer implements OtherTestSmtpMailServer {

    public OtherTestSmtpMailServerImpl() {
        super(
                null,
                NAME,
                DESCRIPTION,
                FROM,
                PREFIX,
                false,
                false,
                PROTOCOL,
                HOST,
                PORT,
                TLS_REQUIRED,
                USERNAME,
                PASSWORD,
                TIMEOUT
        );
    }

}
