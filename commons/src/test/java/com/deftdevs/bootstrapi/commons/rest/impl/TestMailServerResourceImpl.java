package com.deftdevs.bootstrapi.commons.rest.impl;

import com.deftdevs.bootstrapi.commons.rest.AbstractMailServerResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;

public class TestMailServerResourceImpl extends AbstractMailServerResourceImpl {

    public TestMailServerResourceImpl(MailServerService mailServerService) {
        super(mailServerService);
    }
}
