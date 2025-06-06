package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.crowd.directory.DelegatedAuthenticationDirectory;
import com.atlassian.crowd.directory.SynchronisableDirectoryProperties;
import com.atlassian.crowd.directory.ldap.LDAPPropertiesMapper;
import com.atlassian.crowd.directory.ldap.LdapSecureMode;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.embedded.api.MockDirectoryInternal;
import com.atlassian.crowd.embedded.api.OperationType;
import com.atlassian.crowd.model.directory.ImmutableDirectory;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryDelegatingModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryInternalModel;
import com.deftdevs.bootstrapi.commons.model.type.DirectoryPermissions;
import com.deftdevs.bootstrapi.commons.util.AssertUtil;
import com.deftdevs.bootstrapi.crowd.model.util.DirectoryModelUtil.DirectoryDelegatingConnectorTypeImplClass;
import com.deftdevs.bootstrapi.crowd.model.util.DirectoryModelUtil.UnsupportedDirectoryModelException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static com.atlassian.crowd.directory.AbstractInternalDirectory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
public class DirectoryModelUtilTest {

    @Test
    public void testToDirectoryInternalModel() {
        final Directory directory = new MockDirectoryInternal();

        final DirectoryInternalModel directoryInternalModel = (DirectoryInternalModel) DirectoryModelUtil.toDirectoryModel(directory);
        assertDirectoryDetailsMatch(directory, directoryInternalModel, true);
        assertDirectoryInternalAttributesForCredentialPolicyMatch(directory, directoryInternalModel, true);
    }

    @Test
    public void testToDirectoryDelegatingModel() {
        final Directory directory = getDirectoryDelegating();

        final DirectoryDelegatingModel directoryDelegatingModel = (DirectoryDelegatingModel) DirectoryModelUtil.toDirectoryModel(directory);
        assertDirectoryDetailsMatch(directory, directoryDelegatingModel, true);
        assertDirectoryDelegatingAttributesForConnectorMatch(directory, directoryDelegatingModel, true);
        assertDirectoryDelegatingAttributesForConfigurationMatch(directory, directoryDelegatingModel, true);
        assertDirectoryAllowedOperationsMatches(directory.getAllowedOperations(), directoryDelegatingModel.getPermissions(), true);
    }

    @Test
    public void testDirectoryToDirectoryGenericModel() {
        final Directory directory = spy(new MockDirectoryInternal());
        doReturn(DirectoryType.CUSTOM).when(directory).getType();

        final AbstractDirectoryModel directoryModel = DirectoryModelUtil.toDirectoryModel(directory);
        assertDirectoryDetailsMatch(directory, directoryModel, true);
    }

    @Test
    public void testDirectoryInternalModelToDirectory() throws UnsupportedDirectoryModelException {
        final DirectoryInternalModel directoryInternalModel = DirectoryInternalModel.EXAMPLE_1;

        final Directory directory = DirectoryModelUtil.toDirectory(directoryInternalModel);
        assertEquals(DirectoryType.INTERNAL, directory.getType());
        assertDirectoryDetailsMatch(directory, directoryInternalModel, false);
        assertDirectoryInternalAttributesForCredentialPolicyMatch(directory, directoryInternalModel, false);
        assertDirectoryAllowedOperationsMatches(directory.getAllowedOperations(), directoryInternalModel.getPermissions(), false);
    }

    @Test
    public void testDirectoryDelegatingModelToDirectory() throws UnsupportedDirectoryModelException {
        final DirectoryDelegatingModel directoryDelegatingModel = DirectoryDelegatingModel.EXAMPLE_1;

        final Directory directory = DirectoryModelUtil.toDirectory(directoryDelegatingModel);
        assertEquals(DirectoryType.DELEGATING, directory.getType());
        assertDirectoryDetailsMatch(directory, directoryDelegatingModel, false);
        assertDirectoryDelegatingAttributesForConnectorMatch(directory, directoryDelegatingModel, false);
        assertDirectoryDelegatingAttributesForConfigurationMatch(directory, directoryDelegatingModel, false);
        assertDirectoryAllowedOperationsMatches(directory.getAllowedOperations(), directoryDelegatingModel.getPermissions(), false);
    }

    private void assertDirectoryInternalAttributesForCredentialPolicyMatch(
            final Directory directory,
            final DirectoryInternalModel directoryInternalModel,
            final boolean firstParameterIsExpected) {

        AssertUtil.assertEquals(directory.getAttributes().get(ATTRIBUTE_PASSWORD_REGEX), directoryInternalModel.getCredentialPolicy().getPasswordRegex(), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getAttributes().get(ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE), directoryInternalModel.getCredentialPolicy().getPasswordComplexityMessage(), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getAttributes().get(ATTRIBUTE_PASSWORD_MAX_ATTEMPTS), String.valueOf(directoryInternalModel.getCredentialPolicy().getPasswordMaxAttempts()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getAttributes().get(ATTRIBUTE_PASSWORD_HISTORY_COUNT), String.valueOf(directoryInternalModel.getCredentialPolicy().getPasswordHistoryCount()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getAttributes().get(ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME), String.valueOf(directoryInternalModel.getCredentialPolicy().getPasswordMaxChangeTime()), firstParameterIsExpected);
        assertNotNull(directory.getAttributes().get(ATTRIBUTE_PASSWORD_EXPIRATION_NOTIFICATION_PERIODS));
        AssertUtil.assertEquals(directory.getAttributes().get(ATTRIBUTE_USER_ENCRYPTION_METHOD), directoryInternalModel.getCredentialPolicy().getPasswordEncryptionMethod(), firstParameterIsExpected);
    }

    private void assertDirectoryDetailsMatch(
            final Directory directoryActual,
            final AbstractDirectoryModel directoryModelExpected,
            final boolean firstParameterIsExpected) {

        AssertUtil.assertEquals(directoryActual.getId(), directoryModelExpected.getId(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getName(), directoryModelExpected.getName(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getDescription(), directoryModelExpected.getDescription(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.isActive(), directoryModelExpected.getActive(), firstParameterIsExpected);
    }

    private void assertDirectoryDelegatingAttributesForConnectorMatch(
            final Directory directory,
            final DirectoryDelegatingModel directoryDelegatingModel,
            final boolean firstParameterIsExpected) {

        AssertUtil.assertEquals(directory.getValue(DelegatedAuthenticationDirectory.ATTRIBUTE_LDAP_DIRECTORY_CLASS), DirectoryDelegatingConnectorTypeImplClass.MICROSOFT_ACTIVE_DIRECTORY.getImplClass(), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_URL_KEY), directoryDelegatingModel.getConnector().getUrl(), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_SECURE_KEY), LdapSecureMode.valueOf(directoryDelegatingModel.getConnector().getSsl().name()).getName(), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_REFERRAL_KEY), String.valueOf(directoryDelegatingModel.getConnector().getUseNodeReferrals()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_NESTED_GROUPS_DISABLED), String.valueOf(directoryDelegatingModel.getConnector().getNestedGroupsDisabled()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(DelegatedAuthenticationDirectory.ATTRIBUTE_CREATE_USER_ON_AUTH), String.valueOf(directoryDelegatingModel.getConnector().getSynchronizeUsers()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(DelegatedAuthenticationDirectory.ATTRIBUTE_UPDATE_USER_ON_AUTH), String.valueOf(directoryDelegatingModel.getConnector().getSynchronizeUserDetails()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(DelegatedAuthenticationDirectory.ATTRIBUTE_KEY_IMPORT_GROUPS), String.valueOf(directoryDelegatingModel.getConnector().getSynchronizeGroupMemberships()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_USING_USER_MEMBERSHIP_ATTRIBUTE), String.valueOf(directoryDelegatingModel.getConnector().getUseUserMembershipAttribute()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_PAGEDRESULTS_KEY), String.valueOf(directoryDelegatingModel.getConnector().getUsePagedResults()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_PAGEDRESULTS_SIZE), String.valueOf(directoryDelegatingModel.getConnector().getPagedResultsSize()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(SynchronisableDirectoryProperties.READ_TIMEOUT_IN_MILLISECONDS), String.valueOf(directoryDelegatingModel.getConnector().getReadTimeoutInMillis()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_SEARCH_TIMELIMIT), String.valueOf(directoryDelegatingModel.getConnector().getSearchTimeoutInMillis()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(SynchronisableDirectoryProperties.CONNECTION_TIMEOUT_IN_MILLISECONDS), String.valueOf(directoryDelegatingModel.getConnector().getConnectionTimeoutInMillis()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_BASEDN_KEY), directoryDelegatingModel.getConnector().getBaseDn(), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_USERDN_KEY), directoryDelegatingModel.getConnector().getUsername(), firstParameterIsExpected);
    }

    private void assertDirectoryDelegatingAttributesForConfigurationMatch(
            final Directory directoryActual,
            final DirectoryDelegatingModel directoryDelegatingModelExpected,
            final boolean firstParameterIsExpected) {

        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_DN_ADDITION), directoryDelegatingModelExpected.getConfiguration().getUserDn(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_OBJECTCLASS_KEY), directoryDelegatingModelExpected.getConfiguration().getUserObjectClass(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_OBJECTFILTER_KEY), directoryDelegatingModelExpected.getConfiguration().getUserObjectFilter(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_USERNAME_KEY), directoryDelegatingModelExpected.getConfiguration().getUserNameAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_USERNAME_RDN_KEY), directoryDelegatingModelExpected.getConfiguration().getUserNameRdnAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_FIRSTNAME_KEY), directoryDelegatingModelExpected.getConfiguration().getUserFirstNameAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_LASTNAME_KEY), directoryDelegatingModelExpected.getConfiguration().getUserLastNameAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_DISPLAYNAME_KEY), directoryDelegatingModelExpected.getConfiguration().getUserDisplayNameAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_EMAIL_KEY), directoryDelegatingModelExpected.getConfiguration().getUserEmailAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_GROUP_KEY), directoryDelegatingModelExpected.getConfiguration().getUserGroupAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.LDAP_EXTERNAL_ID), directoryDelegatingModelExpected.getConfiguration().getUserUniqueIdAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.GROUP_DN_ADDITION), directoryDelegatingModelExpected.getConfiguration().getGroupDn(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.GROUP_OBJECTCLASS_KEY), directoryDelegatingModelExpected.getConfiguration().getGroupObjectClass(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.GROUP_OBJECTFILTER_KEY), directoryDelegatingModelExpected.getConfiguration().getGroupObjectFilter(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.GROUP_NAME_KEY), directoryDelegatingModelExpected.getConfiguration().getGroupNameAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.GROUP_DESCRIPTION_KEY), directoryDelegatingModelExpected.getConfiguration().getGroupDescriptionAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.GROUP_USERNAMES_KEY), directoryDelegatingModelExpected.getConfiguration().getGroupMembersAttribute(), firstParameterIsExpected);
    }

    private void assertDirectoryAllowedOperationsMatches(
            final Set<OperationType> operationTypes,
            final DirectoryPermissions permissions,
            final boolean firstParameterIsExpected) {

        AssertUtil.assertEquals(operationTypes.contains(OperationType.CREATE_GROUP), permissions.getAddGroup(), firstParameterIsExpected);
        AssertUtil.assertEquals(operationTypes.contains(OperationType.CREATE_USER), permissions.getAddUser(), firstParameterIsExpected);
        AssertUtil.assertEquals(operationTypes.contains(OperationType.UPDATE_GROUP), permissions.getModifyGroup(), firstParameterIsExpected);
        AssertUtil.assertEquals(operationTypes.contains(OperationType.UPDATE_USER), permissions.getModifyUser(), firstParameterIsExpected);
        AssertUtil.assertEquals(operationTypes.contains(OperationType.UPDATE_GROUP_ATTRIBUTE), permissions.getModifyGroup(), firstParameterIsExpected);
        AssertUtil.assertEquals(operationTypes.contains(OperationType.UPDATE_USER_ATTRIBUTE), permissions.getModifyUser(), firstParameterIsExpected);
        AssertUtil.assertEquals(operationTypes.contains(OperationType.DELETE_GROUP), permissions.getRemoveGroup(), firstParameterIsExpected);
        AssertUtil.assertEquals(operationTypes.contains(OperationType.DELETE_USER), permissions.getRemoveUser(), firstParameterIsExpected);
    }

    private Directory getDirectoryDelegating() {
        final ImmutableDirectory.Builder directoryBuilder = ImmutableDirectory.builder("Delegating Directory", DirectoryType.DELEGATING,
                        DirectoryDelegatingConnectorTypeImplClass.MICROSOFT_ACTIVE_DIRECTORY.getImplClass())
                .setId(2L)
                // Don't set any allowed operations, because we have all enabled in the DirectoryDelegatingModel example
                .setAllowedOperations(Collections.emptySet())
                // Connector attributes
                .setAttribute(DelegatedAuthenticationDirectory.ATTRIBUTE_LDAP_DIRECTORY_CLASS, DirectoryDelegatingConnectorTypeImplClass.MICROSOFT_ACTIVE_DIRECTORY.getImplClass())
                .setAttribute(LDAPPropertiesMapper.LDAP_URL_KEY, "ldap://example.com:389")
                .setAttribute(LDAPPropertiesMapper.LDAP_SECURE_KEY, LdapSecureMode.START_TLS.getName())
                .setAttribute(LDAPPropertiesMapper.LDAP_REFERRAL_KEY, String.valueOf(true))
                .setAttribute(LDAPPropertiesMapper.LDAP_NESTED_GROUPS_DISABLED, String.valueOf(false))
                .setAttribute(DelegatedAuthenticationDirectory.ATTRIBUTE_CREATE_USER_ON_AUTH, String.valueOf(false))
                .setAttribute(DelegatedAuthenticationDirectory.ATTRIBUTE_UPDATE_USER_ON_AUTH, String.valueOf(false))
                .setAttribute(DelegatedAuthenticationDirectory.ATTRIBUTE_KEY_IMPORT_GROUPS, String.valueOf(false))
                .setAttribute(LDAPPropertiesMapper.LDAP_USING_USER_MEMBERSHIP_ATTRIBUTE, String.valueOf(false))
                .setAttribute(LDAPPropertiesMapper.LDAP_PAGEDRESULTS_KEY, String.valueOf(true))
                .setAttribute(LDAPPropertiesMapper.LDAP_PAGEDRESULTS_SIZE, String.valueOf(999L))
                .setAttribute(SynchronisableDirectoryProperties.READ_TIMEOUT_IN_MILLISECONDS, String.valueOf(123000L))
                .setAttribute(LDAPPropertiesMapper.LDAP_SEARCH_TIMELIMIT, String.valueOf(456000L))
                .setAttribute(SynchronisableDirectoryProperties.CONNECTION_TIMEOUT_IN_MILLISECONDS, String.valueOf(789000L))
                .setAttribute(LDAPPropertiesMapper.LDAP_BASEDN_KEY, "baseDn")
                .setAttribute(LDAPPropertiesMapper.LDAP_USERDN_KEY, "userDn")
                .setAttribute(LDAPPropertiesMapper.LDAP_PASSWORD_KEY, "password")
                // Configuration attributes
                .setAttribute(LDAPPropertiesMapper.USER_DN_ADDITION, "userDnAddition")
                .setAttribute(LDAPPropertiesMapper.USER_OBJECTCLASS_KEY, "userObjectClass")
                .setAttribute(LDAPPropertiesMapper.USER_OBJECTFILTER_KEY, "userObjectFilter")
                .setAttribute(LDAPPropertiesMapper.USER_USERNAME_KEY, "userName")
                .setAttribute(LDAPPropertiesMapper.USER_USERNAME_RDN_KEY, "userNameRdn")
                .setAttribute(LDAPPropertiesMapper.USER_FIRSTNAME_KEY, "userFirstName")
                .setAttribute(LDAPPropertiesMapper.USER_LASTNAME_KEY, "userLastName")
                .setAttribute(LDAPPropertiesMapper.USER_DISPLAYNAME_KEY, "userDisplayName")
                .setAttribute(LDAPPropertiesMapper.USER_EMAIL_KEY, "userEmail")
                .setAttribute(LDAPPropertiesMapper.USER_GROUP_KEY, "userGroup")
                .setAttribute(LDAPPropertiesMapper.LDAP_EXTERNAL_ID, "userUniqueId")
                .setAttribute(LDAPPropertiesMapper.GROUP_DN_ADDITION, "groupDnAddition")
                .setAttribute(LDAPPropertiesMapper.GROUP_OBJECTCLASS_KEY, "groupObjectClass")
                .setAttribute(LDAPPropertiesMapper.GROUP_OBJECTFILTER_KEY, "groupObjectFilter")
                .setAttribute(LDAPPropertiesMapper.GROUP_NAME_KEY, "groupName")
                .setAttribute(LDAPPropertiesMapper.GROUP_DESCRIPTION_KEY, "groupDescription")
                .setAttribute(LDAPPropertiesMapper.GROUP_USERNAMES_KEY, "groupMembers")
                ;

        return directoryBuilder.build();
    }

}
