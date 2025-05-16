package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.crowd.directory.AbstractInternalDirectory;
import com.atlassian.crowd.directory.DelegatedAuthenticationDirectory;
import com.atlassian.crowd.directory.DirectoryProperties;
import com.atlassian.crowd.directory.InternalDirectory;
import com.atlassian.crowd.directory.MicrosoftActiveDirectory;
import com.atlassian.crowd.directory.SynchronisableDirectoryProperties;
import com.atlassian.crowd.directory.ldap.LDAPPropertiesMapper;
import com.atlassian.crowd.directory.ldap.LdapSecureMode;
import com.atlassian.crowd.directory.monitor.poller.PollerConfig;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.embedded.api.OperationType;
import com.atlassian.crowd.model.directory.DirectoryImpl;
import com.atlassian.crowd.model.directory.ImmutableDirectory;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryCrowdModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryDelegatingModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryGenericModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryInternalModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryLdapModel;
import com.deftdevs.bootstrapi.commons.model.type.DirectoryPermissions;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.deftdevs.bootstrapi.crowd.util.AttributeUtil.*;
import static java.lang.Boolean.TRUE;

public class DirectoryModelUtil {

    public static final String ATTRIBUTE_USE_NESTED_GROUPS = "useNestedGroups";

    private static final Set<Class<? extends AbstractDirectoryModel>> SUPPORTED_DIRECTORY_BEAN_TYPES;

    static {
        final Set<Class<? extends AbstractDirectoryModel>> supportedDirectoryModelTypes = new HashSet<>();
        supportedDirectoryModelTypes.add(DirectoryInternalModel.class);
        supportedDirectoryModelTypes.add(DirectoryDelegatingModel.class);
        SUPPORTED_DIRECTORY_BEAN_TYPES = Collections.unmodifiableSet(supportedDirectoryModelTypes);
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
    public static AbstractDirectoryModel toDirectoryModel(
            @Nonnull final Directory directory) {

        if (directory.getType().equals(DirectoryType.INTERNAL)) {
            return toDirectoryInternalModel(directory);
        } else if (directory.getType().equals(DirectoryType.DELEGATING)) {
            return toDirectoryDelegatingModel(directory);
        }

        return toDirectoryGenericModel(directory);
    }

    @Nonnull
    public static DirectoryInternalModel toDirectoryInternalModel(
            @Nonnull final Directory directory) {

        final DirectoryInternalModel directoryModel = new DirectoryInternalModel();
        setDirectoryModelDetails(directoryModel, directory);

        final Map<String, String> attributes = new HashMap<>(directory.getAttributes());

        directoryModel.setCredentialPolicy(new DirectoryInternalModel.DirectoryInternalCredentialPolicy());
        directoryModel.getCredentialPolicy().setPasswordRegex(attributes.get(AbstractInternalDirectory.ATTRIBUTE_PASSWORD_REGEX));
        directoryModel.getCredentialPolicy().setPasswordComplexityMessage(attributes.get(AbstractInternalDirectory.ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE));
        directoryModel.getCredentialPolicy().setPasswordMaxAttempts(toLong(attributes.get(AbstractInternalDirectory.ATTRIBUTE_PASSWORD_MAX_ATTEMPTS)));
        directoryModel.getCredentialPolicy().setPasswordHistoryCount(toLong(attributes.get(AbstractInternalDirectory.ATTRIBUTE_PASSWORD_HISTORY_COUNT)));
        directoryModel.getCredentialPolicy().setPasswordMaxChangeTime(toLong(attributes.get(AbstractInternalDirectory.ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME)));
        directoryModel.getCredentialPolicy().setPasswordExpiryNotificationDays(toIntegerList(attributes.get(AbstractInternalDirectory.ATTRIBUTE_PASSWORD_EXPIRATION_NOTIFICATION_PERIODS)));
        directoryModel.getCredentialPolicy().setPasswordEncryptionMethod(attributes.get(AbstractInternalDirectory.ATTRIBUTE_USER_ENCRYPTION_METHOD));

        directoryModel.setAdvanced(new DirectoryInternalModel.DirectoryInternalAdvanced());
        directoryModel.getAdvanced().setEnableNestedGroups(toBoolean(attributes.getOrDefault(ATTRIBUTE_USE_NESTED_GROUPS, "false")));

        setDirectoryModelPermissions(directoryModel, directory);

        return directoryModel;
    }

    @Nonnull
    public static DirectoryDelegatingModel toDirectoryDelegatingModel(
            @Nonnull final Directory directory) {

        final DirectoryDelegatingModel directoryModel = new DirectoryDelegatingModel();
        setDirectoryModelDetails(directoryModel, directory);

        directoryModel.setConnector(new DirectoryDelegatingModel.DirectoryDelegatingConnector());
        directoryModel.getConnector().setType(toDirectoryDelegatingConnectorType(directory));
        directoryModel.getConnector().setUrl(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_URL_KEY));
        directoryModel.getConnector().setSsl(toDirectoryDelegatingConnectorSslType(directory));
        directoryModel.getConnector().setUseNodeReferrals(toBoolean(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_REFERRAL_KEY)));
        directoryModel.getConnector().setNestedGroupsDisabled(toBoolean(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_NESTED_GROUPS_DISABLED)));
        directoryModel.getConnector().setSynchronizeUsers(toBoolean(directory.getAttributes().get(DelegatedAuthenticationDirectory.ATTRIBUTE_CREATE_USER_ON_AUTH)));
        directoryModel.getConnector().setSynchronizeUserDetails(toBoolean(directory.getAttributes().get(DelegatedAuthenticationDirectory.ATTRIBUTE_UPDATE_USER_ON_AUTH)));
        directoryModel.getConnector().setSynchronizeGroupMemberships(toBoolean(directory.getAttributes().get(DelegatedAuthenticationDirectory.ATTRIBUTE_KEY_IMPORT_GROUPS)));
        directoryModel.getConnector().setUseUserMembershipAttribute(toBoolean(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_USING_USER_MEMBERSHIP_ATTRIBUTE)));
        directoryModel.getConnector().setUsePagedResults(toBoolean(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_PAGEDRESULTS_KEY)));
        directoryModel.getConnector().setPagedResultsSize(toLong(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_PAGEDRESULTS_SIZE)));
        directoryModel.getConnector().setReadTimeoutInMillis(toLong(directory.getAttributes().get(SynchronisableDirectoryProperties.READ_TIMEOUT_IN_MILLISECONDS)));
        directoryModel.getConnector().setSearchTimeoutInMillis(toLong(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_SEARCH_TIMELIMIT)));
        directoryModel.getConnector().setConnectionTimeoutInMillis(toLong(directory.getAttributes().get(SynchronisableDirectoryProperties.CONNECTION_TIMEOUT_IN_MILLISECONDS)));
        directoryModel.getConnector().setBaseDn(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_BASEDN_KEY));
        directoryModel.getConnector().setUsername(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_USERDN_KEY));

        directoryModel.setConfiguration(new DirectoryDelegatingModel.DirectoryDelegatingConfiguration());
        directoryModel.getConfiguration().setUserDn(directory.getAttributes().get(LDAPPropertiesMapper.USER_DN_ADDITION));
        directoryModel.getConfiguration().setUserObjectClass(directory.getAttributes().get(LDAPPropertiesMapper.USER_OBJECTCLASS_KEY));
        directoryModel.getConfiguration().setUserObjectFilter(directory.getAttributes().get(LDAPPropertiesMapper.USER_OBJECTFILTER_KEY));
        directoryModel.getConfiguration().setUserNameAttribute(directory.getAttributes().get(LDAPPropertiesMapper.USER_USERNAME_KEY));
        directoryModel.getConfiguration().setUserNameRdnAttribute(directory.getAttributes().get(LDAPPropertiesMapper.USER_USERNAME_RDN_KEY));
        directoryModel.getConfiguration().setUserFirstNameAttribute(directory.getAttributes().get(LDAPPropertiesMapper.USER_FIRSTNAME_KEY));
        directoryModel.getConfiguration().setUserLastNameAttribute(directory.getAttributes().get(LDAPPropertiesMapper.USER_LASTNAME_KEY));
        directoryModel.getConfiguration().setUserDisplayNameAttribute(directory.getAttributes().get(LDAPPropertiesMapper.USER_DISPLAYNAME_KEY));
        directoryModel.getConfiguration().setUserEmailAttribute(directory.getAttributes().get(LDAPPropertiesMapper.USER_EMAIL_KEY));
        directoryModel.getConfiguration().setUserGroupAttribute(directory.getAttributes().get(LDAPPropertiesMapper.USER_GROUP_KEY));
        directoryModel.getConfiguration().setUserUniqueIdAttribute(directory.getAttributes().get(LDAPPropertiesMapper.LDAP_EXTERNAL_ID));
        directoryModel.getConfiguration().setGroupDn(directory.getAttributes().get(LDAPPropertiesMapper.GROUP_DN_ADDITION));
        directoryModel.getConfiguration().setGroupObjectClass(directory.getAttributes().get(LDAPPropertiesMapper.GROUP_OBJECTCLASS_KEY));
        directoryModel.getConfiguration().setGroupObjectFilter(directory.getAttributes().get(LDAPPropertiesMapper.GROUP_OBJECTFILTER_KEY));
        directoryModel.getConfiguration().setGroupNameAttribute(directory.getAttributes().get(LDAPPropertiesMapper.GROUP_NAME_KEY));
        directoryModel.getConfiguration().setGroupDescriptionAttribute(directory.getAttributes().get(LDAPPropertiesMapper.GROUP_DESCRIPTION_KEY));
        directoryModel.getConfiguration().setGroupMembersAttribute(directory.getAttributes().get(LDAPPropertiesMapper.GROUP_USERNAMES_KEY));

        setDirectoryModelPermissions(directoryModel, directory);

        return directoryModel;
    }

    @Nonnull
    private static DirectoryDelegatingModel.DirectoryDelegatingConnector.SslType toDirectoryDelegatingConnectorSslType(
            @Nonnull final Directory directory) {

        final String ldapSecure = directory.getAttributes().get(LDAPPropertiesMapper.LDAP_SECURE_KEY);
        // LdapSecureMode.fromString evaluates to the default value NONE ("false") if ldapSecure is null
        final LdapSecureMode ldapSecureMode = LdapSecureMode.fromString(ldapSecure);
        return DirectoryDelegatingModel.DirectoryDelegatingConnector.SslType.valueOf(ldapSecureMode.name().toUpperCase());
    }

    @Nonnull
    private static DirectoryGenericModel toDirectoryGenericModel(
            @Nonnull final Directory directory) {

        final DirectoryGenericModel directoryModel = new DirectoryGenericModel();
        setDirectoryModelDetails(directoryModel, directory);

        return directoryModel;
    }

    /*
     * Methods for converting directory beans to directories.
     */

    /**
     * Build directory.
     *
     * @param directoryModel the directory bean
     * @return the directory
     */
    @Nonnull
    public static Directory toDirectory(
            @Nonnull final AbstractDirectoryModel directoryModel) throws UnsupportedDirectoryModelException {

        final ImmutableDirectory.Builder directoryBuilder = ImmutableDirectory.builder(
                directoryModel.getName(), toDirectoryType(directoryModel), toDirectoryImplClass(directoryModel));

        return toDirectory(directoryModel, directoryBuilder.build());
    }

    /**
     * Build directory.
     *
     * @param directoryModel the directory bean
     * @return the directory
     */
    @Nonnull
    public static Directory toDirectory(
            @Nonnull final AbstractDirectoryModel directoryModel,
            @Nonnull final Directory directory) throws UnsupportedDirectoryModelException {

        if (!SUPPORTED_DIRECTORY_BEAN_TYPES.contains(directoryModel.getClass())) {
            throw new UnsupportedDirectoryModelException(directoryModel.getClass());
        }

        final ImmutableDirectory.Builder directoryBuilder = ImmutableDirectory.builder(directory);

        if (directoryModel.getId() != null) {
            directoryBuilder.setId(directoryModel.getId());
        }
        if (directoryModel.getName() != null) {
            directoryBuilder.setName(directoryModel.getName());
        }
        if (directoryModel.getDescription() != null) {
            directoryBuilder.setDescription(directoryModel.getDescription());
        }
        if (directoryModel.getActive() != null) {
            directoryBuilder.setActive(directoryModel.getActive());
        }

        final Map<String, String> attributes = new HashMap<>(directory.getAttributes());
        final Set<OperationType> allowedOperations = new HashSet<>(directory.getAllowedOperations());

        if (DirectoryInternalModel.class.equals(directoryModel.getClass())) {
            final DirectoryInternalModel directoryInternalModel = (DirectoryInternalModel) directoryModel;
            setDirectoryAttributes(attributes, directoryInternalModel);
            setDirectoryAllowedOperations(allowedOperations, directoryInternalModel);
        } else if (DirectoryDelegatingModel.class.equals(directoryModel.getClass())) {
            final DirectoryDelegatingModel directoryDelegatingModel = (DirectoryDelegatingModel) directoryModel;
            setDirectoryAttributes(attributes, directoryDelegatingModel);
            setDirectoryAllowedOperations(allowedOperations, directoryDelegatingModel);
        }

        return directoryBuilder
                .setAttributes(attributes)
                .setAllowedOperations(allowedOperations)
                .build();
    }

    /*
     * Helper methods for converting directories to directory beans.
     */

    private static void setDirectoryModelDetails(
            @Nonnull final AbstractDirectoryModel directoryModel,
            @Nonnull final Directory directory) {

        directoryModel.setId(directory.getId());
        directoryModel.setName(directory.getName());
        directoryModel.setDescription(directory.getDescription());
        directoryModel.setActive(directory.isActive());
        directoryModel.setCreatedDate(directory.getCreatedDate());
        directoryModel.setUpdatedDate(directory.getUpdatedDate());
    }

    private static void setDirectoryModelPermissions(
            @Nonnull final AbstractDirectoryModel directoryModel,
            @Nonnull final Directory directory) {

        final DirectoryPermissions permissions = toDirectoryPermissions(directory);

        if (DirectoryInternalModel.class.equals(directoryModel.getClass())) {
            DirectoryInternalModel directoryInternalModel = (DirectoryInternalModel) directoryModel;
            directoryInternalModel.setPermissions(permissions);
        } else if (DirectoryDelegatingModel.class.equals(directoryModel.getClass())) {
            DirectoryDelegatingModel directoryDelegatingModel = (DirectoryDelegatingModel) directoryModel;
            directoryDelegatingModel.setPermissions(permissions);
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
    private static DirectoryDelegatingModel.DirectoryDelegatingConnector.ConnectorType toDirectoryDelegatingConnectorType(
            @Nonnull final Directory directory) {

        final String implementationClass = directory.getAttributes().get(DelegatedAuthenticationDirectory.ATTRIBUTE_LDAP_DIRECTORY_CLASS);

        if (implementationClass != null) {
            final DirectoryDelegatingConnectorTypeImplClass implClass = DirectoryDelegatingConnectorTypeImplClass.fromImplClass(implementationClass);

            if (implClass != null) {
                return DirectoryDelegatingModel.DirectoryDelegatingConnector.ConnectorType.valueOf(implClass.name().toUpperCase());
            }
        }

        return null;
    }

    /*
     * Helper methods for converting directory beans to directories.
     */

    @Nonnull
    private static DirectoryType toDirectoryType(
            @Nonnull final AbstractDirectoryModel directoryModel) {

        if (DirectoryInternalModel.class.equals(directoryModel.getClass())) {
            return DirectoryType.INTERNAL;
        } else if (DirectoryCrowdModel.class.equals(directoryModel.getClass())) {
            return DirectoryType.CROWD;
        } else if (DirectoryLdapModel.class.equals(directoryModel.getClass())) {
            return DirectoryType.AZURE_AD;
        } else if (DirectoryDelegatingModel.class.equals(directoryModel.getClass())) {
            return DirectoryType.DELEGATING;
        }

        return DirectoryType.UNKNOWN;
    }

    @Nullable
    private static String toDirectoryImplClass(
            @Nonnull final AbstractDirectoryModel directoryModel) {

        if (DirectoryDelegatingModel.class.equals(directoryModel.getClass())) {
            return DelegatedAuthenticationDirectory.class.getCanonicalName();
        } else if (DirectoryInternalModel.class.equals(directoryModel.getClass())) {
            return InternalDirectory.class.getCanonicalName();
        }

        return null;
    }

    @Nullable
    private static String toDirectoryDelegatedConnectorTypeClass(
            @Nullable final DirectoryDelegatingModel.DirectoryDelegatingConnector.ConnectorType connectorType) {

        if (connectorType == null) {
            return null;
        }

        return DirectoryDelegatingConnectorTypeImplClass.valueOf(connectorType.name()).getImplClass();
    }

    @Nullable
    private static String toDirectoryDelegatingConnectorSecureModeName(
            @Nullable final DirectoryDelegatingModel.DirectoryDelegatingConnector.SslType sslType) {

        if (sslType == null) {
            return null;
        }

        return LdapSecureMode.valueOf(sslType.name()).getName();
    }

    private static void setDirectoryAttributes(
            @Nonnull final Map<String, String> attributes,
            @Nonnull final DirectoryInternalModel directoryInternalModel) {

        final DirectoryInternalModel.DirectoryInternalCredentialPolicy credentialPolicy = directoryInternalModel.getCredentialPolicy();
        if (credentialPolicy != null) {
            setAttributeIfNotNull(attributes, AbstractInternalDirectory.ATTRIBUTE_PASSWORD_REGEX, credentialPolicy.getPasswordRegex());
            setAttributeIfNotNull(attributes, AbstractInternalDirectory.ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE, credentialPolicy.getPasswordComplexityMessage());
            setAttributeIfNotNull(attributes, AbstractInternalDirectory.ATTRIBUTE_PASSWORD_MAX_ATTEMPTS, fromLong(credentialPolicy.getPasswordMaxAttempts()));
            setAttributeIfNotNull(attributes, AbstractInternalDirectory.ATTRIBUTE_PASSWORD_HISTORY_COUNT, fromLong(credentialPolicy.getPasswordHistoryCount()));
            setAttributeIfNotNull(attributes, AbstractInternalDirectory.ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME, fromLong(credentialPolicy.getPasswordMaxChangeTime()));
            setAttributeIfNotNull(attributes, AbstractInternalDirectory.ATTRIBUTE_PASSWORD_EXPIRATION_NOTIFICATION_PERIODS, fromIntegerCollection(credentialPolicy.getPasswordExpiryNotificationDays()));
            setAttributeIfNotNull(attributes, AbstractInternalDirectory.ATTRIBUTE_USER_ENCRYPTION_METHOD, credentialPolicy.getPasswordEncryptionMethod());
        }

        final DirectoryInternalModel.DirectoryInternalAdvanced advanced = directoryInternalModel.getAdvanced();
        if (advanced != null) {
            setAttributeIfNotNull(attributes, ATTRIBUTE_USE_NESTED_GROUPS, fromBoolean(advanced.getEnableNestedGroups()));
        }
    }

    @SuppressWarnings("deprecation")
    private static void setDirectoryAttributes(
            @Nonnull final Map<String, String> attributes,
            @Nonnull final DirectoryDelegatingModel directoryDelegatingModel) {

        final DirectoryDelegatingModel.DirectoryDelegatingConnector connector = directoryDelegatingModel.getConnector();
        if (connector != null) {
            setAttributeIfNotNull(attributes, DelegatedAuthenticationDirectory.ATTRIBUTE_LDAP_DIRECTORY_CLASS, toDirectoryDelegatedConnectorTypeClass(connector.getType()));
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_URL_KEY, connector.getUrl());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_SECURE_KEY, toDirectoryDelegatingConnectorSecureModeName(connector.getSsl()));
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_REFERRAL_KEY, fromBoolean(connector.getUseNodeReferrals()));
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_NESTED_GROUPS_DISABLED, fromBoolean(connector.getNestedGroupsDisabled()));
            setAttributeIfNotNull(attributes, DelegatedAuthenticationDirectory.ATTRIBUTE_CREATE_USER_ON_AUTH, fromBoolean(connector.getSynchronizeUsers()));
            setAttributeIfNotNull(attributes, DelegatedAuthenticationDirectory.ATTRIBUTE_UPDATE_USER_ON_AUTH, fromBoolean(connector.getSynchronizeUserDetails()));
            setAttributeIfNotNull(attributes, DelegatedAuthenticationDirectory.ATTRIBUTE_KEY_IMPORT_GROUPS, fromBoolean(connector.getSynchronizeGroupMemberships()));
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_USING_USER_MEMBERSHIP_ATTRIBUTE, fromBoolean(connector.getUseUserMembershipAttribute()));
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_PAGEDRESULTS_KEY, fromBoolean(connector.getUsePagedResults()));
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_PAGEDRESULTS_SIZE, fromLong(connector.getPagedResultsSize()));
            setAttributeIfNotNull(attributes, SynchronisableDirectoryProperties.READ_TIMEOUT_IN_MILLISECONDS, fromLong(connector.getReadTimeoutInMillis()));
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_SEARCH_TIMELIMIT, fromLong(connector.getSearchTimeoutInMillis()));
            setAttributeIfNotNull(attributes, SynchronisableDirectoryProperties.CONNECTION_TIMEOUT_IN_MILLISECONDS, fromLong(connector.getConnectionTimeoutInMillis()));
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_BASEDN_KEY, connector.getBaseDn());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_USERDN_KEY, connector.getUsername());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.LDAP_PASSWORD_KEY, connector.getPassword());
        }

        final DirectoryDelegatingModel.DirectoryDelegatingConfiguration configuration = directoryDelegatingModel.getConfiguration();
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
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.GROUP_DN_ADDITION, configuration.getGroupDn());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.GROUP_OBJECTCLASS_KEY, configuration.getGroupObjectClass());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.GROUP_OBJECTFILTER_KEY, configuration.getGroupObjectFilter());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.GROUP_NAME_KEY, configuration.getGroupNameAttribute());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.GROUP_DESCRIPTION_KEY, configuration.getGroupDescriptionAttribute());
            setAttributeIfNotNull(attributes, LDAPPropertiesMapper.GROUP_USERNAMES_KEY, configuration.getGroupMembersAttribute());
        }

        // Also set some defaults for directory delegating.
        // It is unclear yet how exactly they are set and whether they can change...
        final PollerConfig pollerConfig = new PollerConfig();
        attributes.putIfAbsent(DirectoryImpl.ATTRIBUTE_KEY_LOCAL_USER_STATUS, Boolean.toString(false));
        attributes.putIfAbsent(DirectoryImpl.ATTRIBUTE_KEY_USE_PRIMARY_GROUP, Boolean.toString(false));
        attributes.putIfAbsent(DirectoryProperties.CACHE_ENABLED, Boolean.toString(false));
        attributes.putIfAbsent(LDAPPropertiesMapper.LDAP_FILTER_EXPIRED_USERS, Boolean.toString(false));
        attributes.putIfAbsent(LDAPPropertiesMapper.LDAP_POOL_TYPE, "JNDI");
        attributes.putIfAbsent(LDAPPropertiesMapper.LDAP_RELAXED_DN_STANDARDISATION, Boolean.toString(false));
        attributes.putIfAbsent(LDAPPropertiesMapper.LDAP_USING_USER_MEMBERSHIP_ATTRIBUTE_FOR_GROUP_MEMBERSHIP, Boolean.toString(false));
        attributes.putIfAbsent(LDAPPropertiesMapper.LOCAL_GROUPS, Boolean.toString(false));
        attributes.putIfAbsent(LDAPPropertiesMapper.ROLES_DISABLED, Boolean.toString(true));
        attributes.putIfAbsent(SynchronisableDirectoryProperties.INCREMENTAL_SYNC_ENABLED, Boolean.toString(false));
        attributes.putIfAbsent(SynchronisableDirectoryProperties.CACHE_SYNCHRONISE_CRON, pollerConfig.getCronExpression());
        attributes.putIfAbsent(SynchronisableDirectoryProperties.CACHE_SYNCHRONISE_INTERVAL, Long.toString(pollerConfig.getPollingIntervalInMin() * 60));
        attributes.putIfAbsent(SynchronisableDirectoryProperties.CACHE_SYNCHRONISATION_TYPE, pollerConfig.getSynchronisationType());
        attributes.putIfAbsent(SynchronisableDirectoryProperties.SYNC_GROUP_MEMBERSHIP_AFTER_SUCCESSFUL_USER_AUTH_ENABLED, SynchronisableDirectoryProperties.SyncGroupMembershipsAfterAuth.DEFAULT.getValue());
    }

    private static void setDirectoryAllowedOperations(
            @Nonnull final Set<OperationType> allowedOperations,
            @Nonnull final AbstractDirectoryModel directoryModel) {

        final DirectoryPermissions permissions;

        if (DirectoryInternalModel.class.equals(directoryModel.getClass())) {
            permissions = ((DirectoryInternalModel) directoryModel).getPermissions();
        } else if (DirectoryDelegatingModel.class.equals(directoryModel.getClass())) {
            permissions = ((DirectoryDelegatingModel) directoryModel).getPermissions();
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
        MICROSOFT_ACTIVE_DIRECTORY(MicrosoftActiveDirectory.class.getCanonicalName());

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

    public static class UnsupportedDirectoryModelException extends Exception {
        public UnsupportedDirectoryModelException(Class<? extends AbstractDirectoryModel> directoryModelClass) {
            super(directoryModelClass.getName());
        }
    }

    private DirectoryModelUtil() {
    }

}
