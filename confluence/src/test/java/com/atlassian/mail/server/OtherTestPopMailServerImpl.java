package com.atlassian.mail.server;

import com.atlassian.confluence.mail.ConfluencePopMailServer;

public class OtherTestPopMailServerImpl extends ConfluencePopMailServer implements OtherTestPopMailServer {

    public OtherTestPopMailServerImpl() {
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
