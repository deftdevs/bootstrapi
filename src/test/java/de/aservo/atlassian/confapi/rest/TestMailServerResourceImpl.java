package de.aservo.atlassian.confapi.rest;

import de.aservo.atlassian.confapi.service.api.MailServerService;

public class TestMailServerResourceImpl extends AbstractMailServerResourceImpl {

    public TestMailServerResourceImpl(MailServerService mailServerService) {
        super(mailServerService);
    }
}
