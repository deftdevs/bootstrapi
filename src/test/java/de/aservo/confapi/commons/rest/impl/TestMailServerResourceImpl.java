package de.aservo.confapi.commons.rest.impl;

import de.aservo.confapi.commons.rest.AbstractMailServerResourceImpl;
import de.aservo.confapi.commons.service.api.MailServerService;

public class TestMailServerResourceImpl extends AbstractMailServerResourceImpl {

    public TestMailServerResourceImpl(MailServerService mailServerService) {
        super(mailServerService);
    }
}
