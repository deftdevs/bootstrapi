package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.junit.AbstractModelTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MailServerPopModelTest extends AbstractModelTest {

    @Override
    protected String getExpectedXmlRootElementName() {
        return BootstrAPI.MAIL_SERVER_POP;
    }
}
