package de.aservo.atlassian.confapi.suite;

import de.aservo.atlassian.confapi.exception.NoContentExceptionTest;
import de.aservo.atlassian.confapi.helper.WebAuthenticationHelperTest;
import de.aservo.atlassian.confapi.model.*;
import de.aservo.atlassian.confapi.service.DirectoryServiceTest;
import de.aservo.atlassian.confapi.util.MailProtocolUtilTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    NoContentExceptionTest.class,
    WebAuthenticationHelperTest.class,
    ErrorCollectionTest.class,
    MailServerPopBeanTest.class,
    SettingsBeanTest.class,
    MailServerSmtpBeanTest.class,
    DirectoryBeanTest.class,

    DirectoryServiceTest.class,

    MailProtocolUtilTest.class
})
public class TestSuite {
    // This class remains empty, it is used only as a holder for the above annotations
}

