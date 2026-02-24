package com.atlassian.mail.server;

import com.atlassian.confluence.jmx.JmxSMTPMailServer;

public class DefaultTestSmtpMailServerImpl extends JmxSMTPMailServer implements DefaultTestSmtpMailServer {

    public DefaultTestSmtpMailServerImpl() {
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
