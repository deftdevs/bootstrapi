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
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryBean;
import com.deftdevs.bootstrapi.commons.model.DirectoryDelegatingBean;
import com.deftdevs.bootstrapi.commons.model.DirectoryInternalBean;
import com.deftdevs.bootstrapi.commons.model.type.DirectoryPermissions;
import com.deftdevs.bootstrapi.crowd.model.util.DirectoryBeanUtil.DirectoryDelegatingConnectorTypeImplClass;
import com.deftdevs.bootstrapi.crowd.model.util.DirectoryBeanUtil.UnsupportedDirectoryBeanException;
import com.deftdevs.bootstrapi.crowd.util.AssertUtil;
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
public class DirectoryBeanUtilTest {

    @Test
    public void testToDirectoryInternalBean() {
        final Directory directory = new MockDirectoryInternal();

        final DirectoryInternalBean directoryInternalBean = (DirectoryInternalBean) DirectoryBeanUtil.toDirectoryBean(directory);
        assertDirectoryDetailsMatch(directory, directoryInternalBean, true);
        assertDirectoryInternalAttributesForCredentialPolicyMatch(directory, directoryInternalBean, true);
    }

    @Test
    public void testToDirectoryDelegatingBean() {
        final Directory directory = getDirectoryDelegating();

        final DirectoryDelegatingBean directoryDelegatingBean = (DirectoryDelegatingBean) DirectoryBeanUtil.toDirectoryBean(directory);
        assertDirectoryDetailsMatch(directory, directoryDelegatingBean, true);
        assertDirectoryDelegatingAttributesForConnectorMatch(directory, directoryDelegatingBean, true);
        assertDirectoryDelegatingAttributesForConfigurationMatch(directory, directoryDelegatingBean, true);
        assertDirectoryAllowedOperationsMatches(directory.getAllowedOperations(), directoryDelegatingBean.getPermissions(), true);
    }

    @Test
    public void testDirectoryToDirectoryGenericBean() {
        final Directory directory = spy(new MockDirectoryInternal());
        doReturn(DirectoryType.CUSTOM).when(directory).getType();

        final AbstractDirectoryBean directoryBean = DirectoryBeanUtil.toDirectoryBean(directory);
        assertDirectoryDetailsMatch(directory, directoryBean, true);
    }

    @Test
    public void testDirectoryInternalBeanToDirectory() throws UnsupportedDirectoryBeanException {
        final DirectoryInternalBean directoryInternalBean = DirectoryInternalBean.EXAMPLE_1;

        final Directory directory = DirectoryBeanUtil.toDirectory(directoryInternalBean);
        assertEquals(DirectoryType.INTERNAL, directory.getType());
        assertDirectoryDetailsMatch(directory, directoryInternalBean, false);
        assertDirectoryInternalAttributesForCredentialPolicyMatch(directory, directoryInternalBean, false);
        assertDirectoryAllowedOperationsMatches(directory.getAllowedOperations(), directoryInternalBean.getPermissions(), false);
    }

    @Test
    public void testDirectoryDelegatingBeanToDirectory() throws UnsupportedDirectoryBeanException {
        final DirectoryDelegatingBean directoryDelegatingBean = DirectoryDelegatingBean.EXAMPLE_1;

        final Directory directory = DirectoryBeanUtil.toDirectory(directoryDelegatingBean);
        assertEquals(DirectoryType.DELEGATING, directory.getType());
        assertDirectoryDetailsMatch(directory, directoryDelegatingBean, false);
        assertDirectoryDelegatingAttributesForConnectorMatch(directory, directoryDelegatingBean, false);
        assertDirectoryDelegatingAttributesForConfigurationMatch(directory, directoryDelegatingBean, false);
        assertDirectoryAllowedOperationsMatches(directory.getAllowedOperations(), directoryDelegatingBean.getPermissions(), false);
    }

    private void assertDirectoryInternalAttributesForCredentialPolicyMatch(
            final Directory directory,
            final DirectoryInternalBean directoryInternalBean,
            final boolean firstParameterIsExpected) {

        AssertUtil.assertEquals(directory.getAttributes().get(ATTRIBUTE_PASSWORD_REGEX), directoryInternalBean.getCredentialPolicy().getPasswordRegex(), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getAttributes().get(ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE), directoryInternalBean.getCredentialPolicy().getPasswordComplexityMessage(), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getAttributes().get(ATTRIBUTE_PASSWORD_MAX_ATTEMPTS), String.valueOf(directoryInternalBean.getCredentialPolicy().getPasswordMaxAttempts()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getAttributes().get(ATTRIBUTE_PASSWORD_HISTORY_COUNT), String.valueOf(directoryInternalBean.getCredentialPolicy().getPasswordHistoryCount()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getAttributes().get(ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME), String.valueOf(directoryInternalBean.getCredentialPolicy().getPasswordMaxChangeTime()), firstParameterIsExpected);
        assertNotNull(directory.getAttributes().get(ATTRIBUTE_PASSWORD_EXPIRATION_NOTIFICATION_PERIODS));
        AssertUtil.assertEquals(directory.getAttributes().get(ATTRIBUTE_USER_ENCRYPTION_METHOD), directoryInternalBean.getCredentialPolicy().getPasswordEncryptionMethod(), firstParameterIsExpected);
    }

    private void assertDirectoryDetailsMatch(
            final Directory directoryActual,
            final AbstractDirectoryBean directoryBeanExpected,
            final boolean firstParameterIsExpected) {

        AssertUtil.assertEquals(directoryActual.getId(), directoryBeanExpected.getId(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getName(), directoryBeanExpected.getName(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getDescription(), directoryBeanExpected.getDescription(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.isActive(), directoryBeanExpected.getActive(), firstParameterIsExpected);
    }

    private void assertDirectoryDelegatingAttributesForConnectorMatch(
            final Directory directory,
            final DirectoryDelegatingBean directoryDelegatingBean,
            final boolean firstParameterIsExpected) {

        AssertUtil.assertEquals(directory.getValue(DelegatedAuthenticationDirectory.ATTRIBUTE_LDAP_DIRECTORY_CLASS), DirectoryDelegatingConnectorTypeImplClass.MICROSOFT_ACTIVE_DIRECTORY.getImplClass(), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_URL_KEY), directoryDelegatingBean.getConnector().getUrl(), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_SECURE_KEY), LdapSecureMode.valueOf(directoryDelegatingBean.getConnector().getSsl().name()).getName(), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_REFERRAL_KEY), String.valueOf(directoryDelegatingBean.getConnector().getUseNodeReferrals()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_NESTED_GROUPS_DISABLED), String.valueOf(directoryDelegatingBean.getConnector().getNestedGroupsDisabled()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(DelegatedAuthenticationDirectory.ATTRIBUTE_CREATE_USER_ON_AUTH), String.valueOf(directoryDelegatingBean.getConnector().getSynchronizeUsers()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(DelegatedAuthenticationDirectory.ATTRIBUTE_UPDATE_USER_ON_AUTH), String.valueOf(directoryDelegatingBean.getConnector().getSynchronizeUserDetails()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(DelegatedAuthenticationDirectory.ATTRIBUTE_KEY_IMPORT_GROUPS), String.valueOf(directoryDelegatingBean.getConnector().getSynchronizeGroupMemberships()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_USING_USER_MEMBERSHIP_ATTRIBUTE), String.valueOf(directoryDelegatingBean.getConnector().getUseUserMembershipAttribute()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_PAGEDRESULTS_KEY), String.valueOf(directoryDelegatingBean.getConnector().getUsePagedResults()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_PAGEDRESULTS_SIZE), String.valueOf(directoryDelegatingBean.getConnector().getPagedResultsSize()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(SynchronisableDirectoryProperties.READ_TIMEOUT_IN_MILLISECONDS), String.valueOf(directoryDelegatingBean.getConnector().getReadTimeoutInMillis()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_SEARCH_TIMELIMIT), String.valueOf(directoryDelegatingBean.getConnector().getSearchTimeoutInMillis()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(SynchronisableDirectoryProperties.CONNECTION_TIMEOUT_IN_MILLISECONDS), String.valueOf(directoryDelegatingBean.getConnector().getConnectionTimeoutInMillis()), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_BASEDN_KEY), directoryDelegatingBean.getConnector().getBaseDn(), firstParameterIsExpected);
        AssertUtil.assertEquals(directory.getValue(LDAPPropertiesMapper.LDAP_USERDN_KEY), directoryDelegatingBean.getConnector().getUsername(), firstParameterIsExpected);
    }

    private void assertDirectoryDelegatingAttributesForConfigurationMatch(
            final Directory directoryActual,
            final DirectoryDelegatingBean directoryDelegatingBeanExpected,
            final boolean firstParameterIsExpected) {

        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_DN_ADDITION), directoryDelegatingBeanExpected.getConfiguration().getUserDn(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_OBJECTCLASS_KEY), directoryDelegatingBeanExpected.getConfiguration().getUserObjectClass(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_OBJECTFILTER_KEY), directoryDelegatingBeanExpected.getConfiguration().getUserObjectFilter(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_USERNAME_KEY), directoryDelegatingBeanExpected.getConfiguration().getUserNameAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_USERNAME_RDN_KEY), directoryDelegatingBeanExpected.getConfiguration().getUserNameRdnAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_FIRSTNAME_KEY), directoryDelegatingBeanExpected.getConfiguration().getUserFirstNameAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_LASTNAME_KEY), directoryDelegatingBeanExpected.getConfiguration().getUserLastNameAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_DISPLAYNAME_KEY), directoryDelegatingBeanExpected.getConfiguration().getUserDisplayNameAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_EMAIL_KEY), directoryDelegatingBeanExpected.getConfiguration().getUserEmailAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.USER_GROUP_KEY), directoryDelegatingBeanExpected.getConfiguration().getUserGroupAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.LDAP_EXTERNAL_ID), directoryDelegatingBeanExpected.getConfiguration().getUserUniqueIdAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.GROUP_DN_ADDITION), directoryDelegatingBeanExpected.getConfiguration().getGroupDn(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.GROUP_OBJECTCLASS_KEY), directoryDelegatingBeanExpected.getConfiguration().getGroupObjectClass(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.GROUP_OBJECTFILTER_KEY), directoryDelegatingBeanExpected.getConfiguration().getGroupObjectFilter(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.GROUP_NAME_KEY), directoryDelegatingBeanExpected.getConfiguration().getGroupNameAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.GROUP_DESCRIPTION_KEY), directoryDelegatingBeanExpected.getConfiguration().getGroupDescriptionAttribute(), firstParameterIsExpected);
        AssertUtil.assertEquals(directoryActual.getValue(LDAPPropertiesMapper.GROUP_USERNAMES_KEY), directoryDelegatingBeanExpected.getConfiguration().getGroupMembersAttribute(), firstParameterIsExpected);
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
                // Don't set any allowed operations, because we have all enabled in the DirectoryDelegatingBean example
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
