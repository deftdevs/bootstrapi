package com.atlassian.mail.server;

import com.atlassian.confluence.mail.ConfluencePopMailServer;

public class DefaultTestPopMailServerImpl extends ConfluencePopMailServer implements DefaultTestPopMailServer {

    public DefaultTestPopMailServerImpl() {
        super(
                null,
                NAME,
                DESCRIPTION,
                PROTOCOL,
                HOST,
                PORT,
                USERNAME,
                PASSWORD,
                null
        );
        setTimeout(TIMEOUT);
    }

}
