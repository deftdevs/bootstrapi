package de.aservo.confapi.crowd.model.util;

import com.atlassian.crowd.directory.AbstractInternalDirectory;
import com.atlassian.crowd.directory.DelegatedAuthenticationDirectory;
import com.atlassian.crowd.directory.SynchronisableDirectoryProperties;
import com.atlassian.crowd.directory.ldap.LDAPPropertiesMapper;
import com.atlassian.crowd.directory.ldap.LdapSecureMode;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.embedded.api.OperationType;
import com.atlassian.crowd.model.directory.ImmutableDirectory;
import de.aservo.confapi.commons.model.AbstractDirectoryBean;
import de.aservo.confapi.commons.model.DirectoryCrowdBean;
import de.aservo.confapi.commons.model.DirectoryDelegatingBean;
import de.aservo.confapi.commons.model.DirectoryGenericBean;
import de.aservo.confapi.commons.model.DirectoryInternalBean;
import de.aservo.confapi.commons.model.DirectoryLdapBean;
import de.aservo.confapi.commons.model.type.DirectoryPermissions;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static de.aservo.confapi.crowd.util.AttributeUtil.*;
import static java.lang.Boolean.TRUE;

public class DirectoryBeanUtil {

    public static final String ATTRIBUTE_USE_NESTED_GROUPS = "useNestedGroups";

    private static final Set<Class<? extends AbstractDirectoryBean>> SUPPORTED_DIRECTORY_BEAN_TYPES;

    static {
        final Set<Class<? extends AbstractDirectoryBean>> supportedDirectoryBeanTypes = new HashSet<>();
        supportedDirectoryBeanTypes.add(DirectoryInternalBean.class);
        supportedDirectoryBeanTypes.add(DirectoryDelegatingBean.class);
        SUPPORTED_DIRECTORY_BEAN_TYPES = Collections.unmodifiableSet(supportedDirectoryBeanTypes);
    }

    /*
     * Methods for converting directories to directory beans.
     */

    /**
     * Build directory bean.
     *
     * @param directory the directory
     * @return the directory bean
     */
    @Nonnull
    public static AbstractDirectoryBean toDirectoryBean(
            @Nonnull final Directory directory) {

        if (directory.getType().equals(DirectoryType.INTERNAL)) {
            return toDirectoryInternalBean(directory);
        } else if (directory.getType().equals(DirectoryType.DELEGATING)) {
            return toDirectoryDelegatingBean(directory);
        }

        return toDirectoryGenericBean(directory);
    }

    @Nonnull
    public static DirectoryInternalBean toDirectoryInternalBean(
            @Nonnull final Directory directory) {

        final DirectoryInternalBean directoryBean = new DirectoryInternalBean();
        setDirectoryBeanDetails(directoryBean, directory);

        final Map<String, String> attributes = new HashMap<>(directory.getAttributes());

        directoryBean.setCredentialPolicy(new DirectoryInternalBean.DirectoryInternalCredentialPolicy());
        directoryBean.getCredentialPolicy().setPasswordRegex(attributes.get(AbstractInternalDirectory.ATTRIBUTE_PASSWORD_REGEX));
        directoryBean.getCredentialPolicy().setPasswordComplexityMessage(attributes.get(AbstractInternalDirectory.ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE));
        directoryBean.getCredentialPolicy().setPasswordMaxAttempts(toLong(attributes.get(AbstractInternalDirectory.ATTRIBUTE_PASSWORD_MAX_ATTEMPTS)));
        directoryBean.getCredentialPolicy().setPasswordHistoryCount(toLong(attributes.get(AbstractInternalDirectory.ATTRIBUTE_PASSWORD_HISTORY_COUNT)));
        directoryBean.getCredentialPolicy().setPasswordMaxChangeTime(toLong(attributes.get(AbstractInternalDirectory.ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME)));
        directoryBean.getCredentialPolicy().setPasswordExpiryNotificationDays(toIntegerList(attributes.get(AbstractInternalDirectory.ATTRIBUTE_PASSWORD_EXPIRATION_NOTIFICATION_PERIODS)));
        directoryBean.getCredentialPolicy().setPasswordEncryptionMethod(attributes.get(AbstractInternalDirectory.ATTRIBUTE_USER_ENCRYPTION_METHOD));

        directoryBean.setAdvanced(new DirectoryInternalBean.DirectoryInternalAdvanced());
        directoryBean.getAdvanced().setEnableNestedGroups(toBoolean(attributes.getOrDefault(ATTRIBUTE_USE_NESTED_GROUPS, "false")));

        setDirectoryBeanPermissions(directoryBean, directory);

        return directoryBean;
    }

    @Nonnull
    public static DirectoryDelegatingBean toDirectoryDelegatingBean(
            @Nonnull final Directory directory) {

        final DirectoryDelegatingBean directoryBean = new DirectoryDelegatingBean();
        setDirectoryBeanDetails(directoryBean, directory);

        directoryBean.setConnector(new DirectoryDelegatingBean.DirectoryDelegatingConnector());
        directoryBean.getConnector().setType(toDirectoryDelegatingConnectorType(directory));
        directoryBean.getConnector().setUrl(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_URL_KEY));
        directoryBean.getConnector().setSsl(toDirectoryDelegatingConnectorSslType(directory));
        directoryBean.getConnector().setUseNodeReferrals(toBoolean(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_REFERRAL_KEY)));
        directoryBean.getConnector().setNestedGroupsDisabled(toBoolean(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_NESTED_GROUPS_DISABLED)));
        directoryBean.getConnector().setSynchronizeUserDetails(toBoolean(directory.getAttributes().get(SynchronisableDirectoryProperties.INCREMENTAL_SYNC_ENABLED)));
        directoryBean.getConnector().setSynchronizeGroupMemberships(toBoolean(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_USING_USER_MEMBERSHIP_ATTRIBUTE_FOR_GROUP_MEMBERSHIP)));
        directoryBean.getConnector().setUsePagedResults(toBoolean(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_PAGEDRESULTS_KEY)));
        directoryBean.getConnector().setPagedResultsSize(toLong(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_PAGEDRESULTS_SIZE)));
        directoryBean.getConnector().setReadTimeoutInMillis(toLong(directory.getAttributes().get(SynchronisableDirectoryProperties.READ_TIMEOUT_IN_MILLISECONDS)));
        directoryBean.getConnector().setSearchTimeoutInMillis(toLong(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_SEARCH_TIMELIMIT)));
        directoryBean.getConnector().setConnectionTimeoutInMillis(toLong(directory.getAttributes().get(SynchronisableDirectoryProperties.CONNECTION_TIMEOUT_IN_MILLISECONDS)));
        directoryBean.getConnector().setBaseDn(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_BASEDN_KEY));
        directoryBean.getConnector().setUsername(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_USERDN_KEY));

        directoryBean.setConfiguration(new DirectoryDelegatingBean.DirectoryDelegatingConfiguration());
        directoryBean.getConfiguration().setUserDn(directory.getAttributes().get(LDAPPropertiesMapper.USER_DN_ADDITION));
        directoryBean.getConfiguration().setUserObjectClass(directory.getAttributes().get(LDAPPropertiesMapper.USER_OBJECTCLASS_KEY));
        directoryBean.getConfiguration().setUserObjectFilter(directory.getAttributes().get(LDAPPropertiesMapper.USER_OBJECTFILTER_KEY));
        directoryBean.getConfiguration().setUserNameAttribute(directory.getAttributes().get(LDAPPropertiesMapper.USER_USERNAME_KEY));
        directoryBean.getConfiguration().setUserNameRdnAttribute(directory.getAttributes().get(LDAPPropertiesMapper.USER_USERNAME_RDN_KEY));
        directoryBean.getConfiguration().setUserFirstNameAttribute(directory.getAttributes().get(LDAPPropertiesMapper.USER_FIRSTNAME_KEY));
        directoryBean.getConfiguration().setUserLastNameAttribute(directory.getAttributes().get(LDAPPropertiesMapper.USER_LASTNAME_KEY));
        directoryBean.getConfiguration().setUserDisplayNameAttribute(directory.getAttributes().get(LDAPPropertiesMapper.USER_DISPLAYNAME_KEY));
        directoryBean.getConfiguration().setUserEmailAttribute(directory.getAttributes().get(LDAPPropertiesMapper.USER_EMAIL_KEY));
        directoryBean.getConfiguration().setUserGroupAttribute(directory.getAttributes().get(LDAPPropertiesMapper.USER_GROUP_KEY));
        directoryBean.getConfiguration().setUserUniqueIdAttribute(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_EXTERNAL_ID));

        setDirectoryBeanPermissions(directoryBean, directory);

        return directoryBean;
    }

    @Nonnull
    private static DirectoryDelegatingBean.DirectoryDelegatingConnector.SslType toDirectoryDelegatingConnectorSslType(
            @Nonnull final Directory directory) {

        final String ldapSecure = directory.getAttributes().get(LDAPPropertiesMapper.LDAP_SECURE_KEY);
        // LdapSecureMode.fromString evaluates to the default value NONE ("false") if ldapSecure is null
        final LdapSecureMode ldapSecureMode = LdapSecureMode.fromString(ldapSecure);
        return DirectoryDelegatingBean.DirectoryDelegatingConnector.SslType.valueOf(ldapSecureMode.name().toUpperCase());
    }

    @Nonnull
    private static DirectoryGenericBean toDirectoryGenericBean(
            @Nonnull final Directory directory) {

        final DirectoryGenericBean directoryBean = new DirectoryGenericBean();
        setDirectoryBeanDetails(directoryBean, directory);

        return directoryBean;
    }

    /*
     * Methods for converting directory beans to directories.
     */

    /**
     * Build directory.
     *
     * @param directoryBean the directory bean
     * @return the directory
     */
    @Nonnull
    public static Directory toDirectory(
            @Nonnull final AbstractDirectoryBean directoryBean) throws UnsupportedDirectoryBeanException {

        final ImmutableDirectory.Builder directoryBuilder = ImmutableDirectory.builder(
                directoryBean.getName(), toDirectoryType(directoryBean), toDirectoryImplClass(directoryBean));

        return toDirectory(directoryBean, directoryBuilder.build());
    }

    /**
     * Build directory.
     *
     * @param directoryBean the directory bean
     * @return the directory
     */
    @Nonnull
    public static Directory toDirectory(
            @Nonnull final AbstractDirectoryBean directoryBean,
            @Nonnull final Directory directory) throws UnsupportedDirectoryBeanException {

        if (!SUPPORTED_DIRECTORY_BEAN_TYPES.contains(directoryBean.getClass())) {
            throw new UnsupportedDirectoryBeanException(directoryBean.getClass());
        }

        final ImmutableDirectory.Builder directoryBuilder = ImmutableDirectory.builder(directory);

        if (directoryBean.getId() != null) {
            directoryBuilder.setId(directoryBean.getId());
        }
        if (directoryBean.getName() != null) {
            directoryBuilder.setName(directoryBean.getName());
        }
        if (directoryBean.getDescription() != null) {
            directoryBuilder.setDescription(directoryBean.getDescription());
        }
        if (directoryBean.getActive() != null) {
            directoryBuilder.setActive(directoryBean.getActive());
        }

        final Map<String, String> attributes = new HashMap<>(directory.getAttributes());
        final Set<OperationType> allowedOperations = new HashSet<>(directory.getAllowedOperations());

        if (DirectoryInternalBean.class.equals(directoryBean.getClass())) {
            final DirectoryInternalBean directoryInternalBean = (DirectoryInternalBean) directoryBean;
            setDirectoryAttributes(attributes, directoryInternalBean);
            setDirectoryAllowedOperations(allowedOperations, directoryInternalBean);
        } else if (DirectoryDelegatingBean.class.equals(directoryBean.getClass())) {
            final DirectoryDelegatingBean directoryDelegatingBean = (DirectoryDelegatingBean) directoryBean;
            setDirectoryAttributes(attributes, directoryDelegatingBean);
            setDirectoryAllowedOperations(allowedOperations, directoryDelegatingBean);
        }

        return directoryBuilder
                .setAttributes(attributes)
                .setAllowedOperations(allowedOperations)
                .build();
    }

    /*
     * Helper methods for converting directories to directory beans.
     */

    private static void setDirectoryBeanDetails(
            @Nonnull final AbstractDirectoryBean directoryBean,
            @Nonnull final Directory directory) {

        directoryBean.setId(directory.getId());
        directoryBean.setName(directory.getName());
        directoryBean.setDescription(directory.getDescription());
        directoryBean.setActive(directory.isActive());
        directoryBean.setCreatedDate(directory.getCreatedDate());
        directoryBean.setUpdatedDate(directory.getUpdatedDate());
    }

    private static void setDirectoryBeanPermissions(
            @Nonnull final AbstractDirectoryBean directoryBean,
            @Nonnull final Directory directory) {

        final DirectoryPermissions permissions = toDirectoryPermissions(directory);

        if (DirectoryInternalBean.class.equals(directoryBean.getClass())) {
            DirectoryInternalBean directoryInternalBean = (DirectoryInternalBean) directoryBean;
            directoryInternalBean.setPermissions(permissions);
        } else if (DirectoryDelegatingBean.class.equals(directoryBean.getClass())) {
            DirectoryDelegatingBean directoryDelegatingBean = (DirectoryDelegatingBean) directoryBean;
            directoryDelegatingBean.setPermissions(permissions);
        }
    }

    @Nonnull
    private static DirectoryPermissions toDirectoryPermissions(
            @Nonnull final Directory directory) {

        final Set<OperationType> allowedOperations = directory.getAllowedOperations();

        final DirectoryPermissions permissions = new DirectoryPermissions();
        permissions.setAddGroup(allowedOperations.contains(OperationType.CREATE_GROUP));
        permissions.setAddUser(allowedOperations.contains(OperationType.CREATE_USER));
        permissions.setModifyGroup(allowedOperations.contains(OperationType.UPDATE_GROUP));
        permissions.setModifyUser(allowedOperations.contains(OperationType.UPDATE_USER));
        permissions.setModifyGroupAttributes(allowedOperations.contains(OperationType.UPDATE_GROUP_ATTRIBUTE));
        permissions.setModifyUserAttributes(allowedOperations.contains(OperationType.UPDATE_USER_ATTRIBUTE));
        permissions.setRemoveGroup(allowedOperations.contains(OperationType.DELETE_GROUP));
        permissions.setRemoveUser(allowedOperations.contains(OperationType.DELETE_USER));

        return permissions;
    }

    // There is no API from Crowd to get the connector type of a directory,
    // so we need to implement all of it ourselves...
    @Nullable
    private static DirectoryDelegatingBean.DirectoryDelegatingConnector.ConnectorType toDirectoryDelegatingConnectorType(
            @Nonnull final Directory directory) {

        final String implementationClass = directory.getAttributes().get(DelegatedAuthenticationDirectory.ATTRIBUTE_LDAP_DIRECTORY_CLASS);

        if (implementationClass != null) {
            final DirectoryDelegatingConnectorTypeImplClass implClass = DirectoryDelegatingConnectorTypeImplClass.fromImplClass(implementationClass);

            if (implClass != null) {
                return DirectoryDelegatingBean.DirectoryDelegatingConnector.ConnectorType.valueOf(implClass.name().toUpperCase());
            }
        }

        return null;
    }

    /*
     * Helper methods for converting directory beans to directories.
     */

    @Nonnull
    private static DirectoryType toDirectoryType(
            @Nonnull final AbstractDirectoryBean directoryBean) {

        if (DirectoryInternalBean.class.equals(directoryBean.getClass())) {
            return DirectoryType.INTERNAL;
        } else if (DirectoryCrowdBean.class.equals(directoryBean.getClass())) {
            return DirectoryType.CROWD;
        } else if (DirectoryLdapBean.class.equals(directoryBean.getClass())) {
            return DirectoryType.AZURE_AD;
        } else if (DirectoryDelegatingBean.class.equals(directoryBean.getClass())) {
            return DirectoryType.DELEGATING;
        }

        return DirectoryType.UNKNOWN;
    }

    @Nullable
    private static String toDirectoryImplClass(
            @Nonnull final AbstractDirectoryBean directoryBean) {

        if (DirectoryDelegatingBean.class.equals(directoryBean.getClass())) {
            final DirectoryDelegatingBean directoryDelegatingBean = (DirectoryDelegatingBean) directoryBean;
            return toDirectoryDelegatedConnectorTypeImplClass(directoryDelegatingBean.getConnector().getType());
        } else if (DirectoryInternalBean.class.equals(directoryBean.getClass())) {
            return "com.atlassian.crowd.directory.InternalDirectory";
        }

        return null;
    }

    @Nullable
    private static String toDirectoryDelegatedConnectorTypeImplClass(
            @Nullable final DirectoryDelegatingBean.DirectoryDelegatingConnector.ConnectorType connectorType) {

        if (connectorType == null) {
            return null;
        }

        return DirectoryDelegatingConnectorTypeImplClass.valueOf(connectorType.name()).getImplClass();
    }

    @Nullable
    private static String toDirectoryDelegatingConnectorSecureModeName(
            @Nullable final DirectoryDelegatingBean.DirectoryDelegatingConnector.SslType sslType) {

        if (sslType == null) {
            return null;
        }

        return LdapSecureMode.valueOf(sslType.name()).getName();
    }

    private static void setDirectoryAttributes(
            @Nonnull final Map<String, String> attributes,
            @Nonnull final DirectoryInternalBean directoryInternalBean) {

        final DirectoryInternalBean.DirectoryInternalCredentialPolicy credentialPolicy = directoryInternalBean.getCredentialPolicy();
        if (credentialPolicy != null) {
            setAttributeIfNotNull(attributes, AbstractInternalDirectory.ATTRIBUTE_PASSWORD_REGEX, credentialPolicy.getPasswordRegex());
            setAttributeIfNotNull(attributes, AbstractInternalDirectory.ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE, credentialPolicy.getPasswordComplexityMessage());
            setAttributeIfNotNull(attributes, AbstractInternalDirectory.ATTRIBUTE_PASSWORD_MAX_ATTEMPTS, fromLong(credentialPolicy.getPasswordMaxAttempts()));
            setAttributeIfNotNull(attributes, AbstractInternalDirectory.ATTRIBUTE_PASSWORD_HISTORY_COUNT, fromLong(credentialPolicy.getPasswordHistoryCount()));
            setAttributeIfNotNull(attributes, AbstractInternalDirectory.ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME, fromLong(credentialPolicy.getPasswordMaxChangeTime()));
            setAttributeIfNotNull(attributes, AbstractInternalDirectory.ATTRIBUTE_PASSWORD_EXPIRATION_NOTIFICATION_PERIODS, fromIntegerList(credentialPolicy.getPasswordExpiryNotificationDays()));
            setAttributeIfNotNull(attributes, AbstractInternalDirectory.ATTRIBUTE_USER_ENCRYPTION_METHOD, credentialPolicy.getPasswordEncryptionMethod());
        }

        final DirectoryInternalBean.DirectoryInternalAdvanced advanced = directoryInternalBean.getAdvanced();
        if (advanced != null) {
            setAttributeIfNotNull(attributes, ATTRIBUTE_USE_NESTED_GROUPS, fromBoolean(advanced.getEnableNestedGroups()));
        }
    }

    private static void setDirectoryAttributes(
            @Nonnull final Map<String, String> attributes,
            @Nonnull final DirectoryDelegatingBean directoryDelegatingBean) {

        final DirectoryDelegatingBean.DirectoryDelegatingConnector connector = directoryDelegatingBean.getConnector();
        if (connector != null) {
            setAttributeIfNotNull(attributes, DelegatedAuthenticationDirectory.ATTRIBUTE_LDAP_DIRECTORY_CLASS, toDirectoryDelegatedConnectorTypeImplClass(connector.getType()));
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_URL_KEY, connector.getUrl());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_SECURE_KEY, toDirectoryDelegatingConnectorSecureModeName(connector.getSsl()));
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_REFERRAL_KEY, fromBoolean(connector.getUseNodeReferrals()));
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_NESTED_GROUPS_DISABLED, fromBoolean(connector.getNestedGroupsDisabled()));
            setAttributeIfNotNull(attributes, SynchronisableDirectoryProperties.INCREMENTAL_SYNC_ENABLED, fromBoolean(connector.getSynchronizeUserDetails()));
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_USING_USER_MEMBERSHIP_ATTRIBUTE_FOR_GROUP_MEMBERSHIP, fromBoolean(connector.getSynchronizeGroupMemberships()));
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_PAGEDRESULTS_KEY, fromBoolean(connector.getUsePagedResults()));
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_PAGEDRESULTS_SIZE, fromLong(connector.getPagedResultsSize()));
            setAttributeIfNotNull(attributes, SynchronisableDirectoryProperties.READ_TIMEOUT_IN_MILLISECONDS, fromLong(connector.getReadTimeoutInMillis()));
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_SEARCH_TIMELIMIT, fromLong(connector.getSearchTimeoutInMillis()));
            setAttributeIfNotNull(attributes, SynchronisableDirectoryProperties.CONNECTION_TIMEOUT_IN_MILLISECONDS, fromLong(connector.getConnectionTimeoutInMillis()));
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_BASEDN_KEY, connector.getBaseDn());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_USERDN_KEY, connector.getUsername());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_PASSWORD_KEY, connector.getPassword());
        }

        final DirectoryDelegatingBean.DirectoryDelegatingConfiguration configuration = directoryDelegatingBean.getConfiguration();
        if (configuration != null) {
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.USER_DN_ADDITION, configuration.getUserDn());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.USER_OBJECTCLASS_KEY, configuration.getUserObjectClass());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.USER_OBJECTFILTER_KEY, configuration.getUserObjectFilter());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.USER_USERNAME_KEY, configuration.getUserNameAttribute());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.USER_USERNAME_RDN_KEY, configuration.getUserNameRdnAttribute());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.USER_FIRSTNAME_KEY, configuration.getUserFirstNameAttribute());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.USER_LASTNAME_KEY, configuration.getUserLastNameAttribute());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.USER_DISPLAYNAME_KEY, configuration.getUserDisplayNameAttribute());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.USER_EMAIL_KEY, configuration.getUserEmailAttribute());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.USER_GROUP_KEY, configuration.getUserGroupAttribute());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_EXTERNAL_ID, configuration.getUserUniqueIdAttribute());
        }
    }

    private static void setDirectoryAllowedOperations(
            @Nonnull final Set<OperationType> allowedOperations,
            @Nonnull final AbstractDirectoryBean directoryBean) {

        final DirectoryPermissions permissions;

        if (DirectoryInternalBean.class.equals(directoryBean.getClass())) {
            permissions = ((DirectoryInternalBean) directoryBean).getPermissions();
        } else if (DirectoryDelegatingBean.class.equals(directoryBean.getClass())) {
            permissions = ((DirectoryDelegatingBean) directoryBean).getPermissions();
        } else {
            permissions = null;
        }

        if (permissions != null) {
            setAllowedOperationIfNotNull(allowedOperations, OperationType.CREATE_GROUP, permissions.getAddGroup());
            setAllowedOperationIfNotNull(allowedOperations, OperationType.CREATE_USER, permissions.getAddUser());
            setAllowedOperationIfNotNull(allowedOperations, OperationType.UPDATE_GROUP, permissions.getModifyGroup());
            setAllowedOperationIfNotNull(allowedOperations, OperationType.UPDATE_USER, permissions.getModifyUser());
            setAllowedOperationIfNotNull(allowedOperations, OperationType.UPDATE_GROUP_ATTRIBUTE, permissions.getModifyGroupAttributes());
            setAllowedOperationIfNotNull(allowedOperations, OperationType.UPDATE_USER_ATTRIBUTE, permissions.getModifyUserAttributes());
            setAllowedOperationIfNotNull(allowedOperations, OperationType.DELETE_GROUP, permissions.getRemoveGroup());
            setAllowedOperationIfNotNull(allowedOperations, OperationType.DELETE_USER, permissions.getRemoveUser());
        }
    }

    /*
     * Other helper methods.
     */

    private static void setAttributeIfNotNull(
            final Map<String, String> attributes,
            final String attribute,
            final String value) {

        if (value != null) {
            attributes.put(attribute, value);
        }
    }

    private static void setAllowedOperationIfNotNull(
            final Set<OperationType> allowedOperations,
            final OperationType operationType,
            final Boolean permission) {

        if (permission != null) {
            allowedOperations.remove(operationType);

            if (TRUE.equals(permission)) {
                allowedOperations.add(operationType);
            }
        }
    }

    @Getter
    enum DirectoryDelegatingConnectorTypeImplClass {
        MICROSOFT_ACTIVE_DIRECTORY("com.atlassian.crowd.directory.MicrosoftActiveDirectory");

        private final String implClass;

        DirectoryDelegatingConnectorTypeImplClass(String implClass) {
            this.implClass = implClass;
        }

        public static DirectoryDelegatingConnectorTypeImplClass fromImplClass(String implClass) {
            for (DirectoryDelegatingConnectorTypeImplClass directoryDelegatingConnectorTypeImplClass : DirectoryDelegatingConnectorTypeImplClass.values()) {
                if (directoryDelegatingConnectorTypeImplClass.getImplClass().equals(implClass)) {
                    return directoryDelegatingConnectorTypeImplClass;
                }
            }
            return null;
        }
    }

    public static class UnsupportedDirectoryBeanException extends Exception {
        public UnsupportedDirectoryBeanException(Class<? extends AbstractDirectoryBean> directoryBeanClass) {
            super(directoryBeanClass.getName());
        }
    }

    private DirectoryBeanUtil() {
    }

}
