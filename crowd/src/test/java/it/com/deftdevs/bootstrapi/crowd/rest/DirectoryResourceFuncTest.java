package it.com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryDelegatingModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.com.deftdevs.bootstrapi.commons.rest.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public class DirectoryResourceFuncTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCreateGetAndDeleteDirectoryDelegating() throws Exception {
        final DirectoryDelegatingModel directoryModel = getExampleDelegatingModel();

        // Create
        final HttpResponse<String> createResponse = HttpRequestHelper.builder(BootstrAPI.DIRECTORY)
                .request(HttpMethod.POST, directoryModel);
        assertEquals(Response.Status.OK.getStatusCode(), createResponse.statusCode());

        final DirectoryDelegatingModel createdDirectory = objectMapper.readValue(
                createResponse.body(), DirectoryDelegatingModel.class);
        assertNotNull(createdDirectory.getId());
        assertEquals(directoryModel.getName(), createdDirectory.getName());
        assertEquals(directoryModel.getActive(), createdDirectory.getActive());
        assertNotNull(createdDirectory.getConnector());
        assertEquals(directoryModel.getConnector().getUrl(), createdDirectory.getConnector().getUrl());
        assertEquals(directoryModel.getConnector().getSsl(), createdDirectory.getConnector().getSsl());
        assertEquals(directoryModel.getConnector().getBaseDn(), createdDirectory.getConnector().getBaseDn());

        // Get
        final HttpResponse<String> getResponse = HttpRequestHelper.builder(BootstrAPI.DIRECTORY + "/" + createdDirectory.getId())
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), getResponse.statusCode());

        final AbstractDirectoryModel retrievedDirectory = objectMapper.readValue(
                getResponse.body(), AbstractDirectoryModel.class);
        assertInstanceOf(DirectoryDelegatingModel.class, retrievedDirectory);
        assertEquals(createdDirectory.getId(), retrievedDirectory.getId());
        assertEquals(createdDirectory.getName(), retrievedDirectory.getName());

        // Delete
        final HttpResponse<String> deleteResponse = HttpRequestHelper.builder(BootstrAPI.DIRECTORY + "/" + createdDirectory.getId())
                .request(HttpMethod.DELETE, null);
        assertEquals(Response.Status.OK.getStatusCode(), deleteResponse.statusCode());

        // Verify deleted
        final HttpResponse<String> getAfterDeleteResponse = HttpRequestHelper.builder(BootstrAPI.DIRECTORY + "/" + createdDirectory.getId())
                .request();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), getAfterDeleteResponse.statusCode());
    }

    @Test
    void testCreateDirectoryDelegatingUnauthenticated() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.DIRECTORY)
                .username("wrong")
                .password("password")
                .request(HttpMethod.POST, getExampleDelegatingModel());
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.statusCode());
    }

    @Test
    void testCreateDirectoryDelegatingUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.DIRECTORY)
                .username("user")
                .password("user")
                .request(HttpMethod.POST, getExampleDelegatingModel());
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.statusCode());
    }

    private DirectoryDelegatingModel getExampleDelegatingModel() {
        return DirectoryDelegatingModel.builder()
                .name("Test Delegating Directory")
                .active(true)
                .description("Integration test directory")
                .connector(DirectoryDelegatingModel.DirectoryDelegatingConnector.builder()
                        .type(DirectoryDelegatingModel.DirectoryDelegatingConnector.ConnectorType.MICROSOFT_ACTIVE_DIRECTORY)
                        .url("ldaps://test.example.com:636")
                        .ssl(DirectoryDelegatingModel.DirectoryDelegatingConnector.SslType.LDAPS)
                        .useNodeReferrals(false)
                        .nestedGroupsDisabled(true)
                        .synchronizeUsers(false)
                        .synchronizeUserDetails(false)
                        .synchronizeGroupMemberships(false)
                        .useUserMembershipAttribute(false)
                        .usePagedResults(true)
                        .pagedResultsSize(999L)
                        .readTimeoutInMillis(120000L)
                        .searchTimeoutInMillis(60000L)
                        .connectionTimeoutInMillis(10000L)
                        .baseDn("DC=test,DC=example,DC=com")
                        .username("domain\\testuser")
                        .password("testpassword")
                        .build())
                .configuration(DirectoryDelegatingModel.DirectoryDelegatingConfiguration.builder()
                        .userDn("")
                        .userObjectClass("user")
                        .userObjectFilter("(objectClass=user)")
                        .userNameAttribute("sAMAccountName")
                        .userNameRdnAttribute("cn")
                        .userFirstNameAttribute("givenName")
                        .userLastNameAttribute("sn")
                        .userDisplayNameAttribute("displayName")
                        .userEmailAttribute("email")
                        .userGroupAttribute("memberOf")
                        .userUniqueIdAttribute("userID")
                        .groupDn("")
                        .groupObjectClass("group")
                        .groupObjectFilter("(objectClass=group)")
                        .groupNameAttribute("gn")
                        .groupDescriptionAttribute("description")
                        .groupMembersAttribute("member")
                        .build())
                .build();
    }
}
