package it.de.aservo.atlassian.confapi.rest;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.confapi.model.LicenseBean;
import de.aservo.atlassian.confapi.model.LicensesBean;
import org.apache.wink.client.ClientAuthenticationException;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.Collection;

import static org.junit.Assert.*;

public abstract class AbstractLicenseResourceFuncTest {

    @Test
    public void testGetLicenses() {
        Resource licensesResource = ResourceBuilder.builder(ConfAPI.LICENSES).build();

        ClientResponse clientResponse = licensesResource.get();
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());

        Collection<LicenseBean> licenses = clientResponse.getEntity(LicensesBean.class).getLicenses();
        assertNotNull(licenses);
        assertNotEquals(0, licenses.size());
        assertNotNull(licenses.iterator().next().getOrganization());
    }

    @Test
    public void testSetLicenses() {
        Resource licensesResource = ResourceBuilder.builder(ConfAPI.LICENSES).build();

        ClientResponse response = licensesResource.put(getExampleBean());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());

        response = licensesResource.get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertEquals(getExampleBean().getLicenses().iterator().next().getDescription(),
                response.getEntity(LicensesBean.class).getLicenses().iterator().next().getDescription());
    }

    @Test
    public void testAddLicenses() {
        Resource licensesResource = ResourceBuilder.builder(ConfAPI.LICENSES).build();

        LicenseBean licenseBean = getExampleBean().getLicenses().iterator().next();
        ClientResponse response = licensesResource.post(licenseBean);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());

        response = licensesResource.get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertEquals(licenseBean.getDescription(),
                response.getEntity(LicensesBean.class).getLicenses().iterator().next().getDescription());
    }

    @Test(expected = ClientAuthenticationException.class)
    public void testGetLicensesUnauthenticated() {
        Resource licensesResource = ResourceBuilder.builder(ConfAPI.LICENSES)
                .username("wrong")
                .password("password")
                .build();
        licensesResource.get();
    }

    @Test(expected = ClientAuthenticationException.class)
    public void testSetLicensesUnauthenticated() {
        Resource licensesResource = ResourceBuilder.builder(ConfAPI.LICENSES)
                .username("wrong")
                .password("password")
                .build();
        licensesResource.put(getExampleBean());
    }

    @Test
    @Ignore("cannot be executed because there is no default user with restricted access rights")
    public void testGetLicensesUnauthorized() {
        Resource licensesResource = ResourceBuilder.builder(ConfAPI.LICENSES)
                .username("user")
                .password("user")
                .build();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), licensesResource.get().getStatusCode());
    }

    @Test
    @Ignore("cannot be executed because there is no default user with restricted access rights")
    public void testSetLicensesUnauthorized() {
        Resource licensesResource = ResourceBuilder.builder(ConfAPI.LICENSES)
                .username("user")
                .password("user")
                .build();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), licensesResource.put(getExampleBean()).getStatusCode());
    }

    protected LicensesBean getExampleBean() {
        return LicensesBean.EXAMPLE_2_DEVELOPER_LICENSE;
    }
}
