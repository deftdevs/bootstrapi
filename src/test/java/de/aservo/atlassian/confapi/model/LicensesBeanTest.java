package de.aservo.atlassian.confapi.model;

import com.atlassian.sal.api.license.SingleProductLicenseDetailsView;
import de.aservo.atlassian.confapi.junit.AbstractBeanTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class LicensesBeanTest extends AbstractBeanTest {

    @Test
    public void testParameterConstructor() {
        SingleProductLicenseDetailsView license = mock(SingleProductLicenseDetailsView.class);
        LicenseBean licenseBean = new LicenseBean(license);
        licenseBean.setProducts(Collections.singletonList("JIRA"));
        licenseBean.setKey("ABC");
        LicensesBean licensesBean = new LicensesBean();
        licensesBean.setLicenses(Collections.singletonList(licenseBean));
        assertNotNull(licensesBean.getLicenses());
    }

}
