package de.aservo.confapi.crowd.model.util;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.embedded.api.OperationType;
import com.atlassian.crowd.model.directory.ImmutableDirectory;
import de.aservo.confapi.commons.model.AbstractDirectoryBean;
import de.aservo.confapi.commons.model.DirectoryCrowdBean;
import de.aservo.confapi.commons.model.DirectoryGenericBean;
import de.aservo.confapi.commons.model.DirectoryInternalBean;
import de.aservo.confapi.commons.model.DirectoryLdapBean;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.atlassian.crowd.directory.AbstractInternalDirectory.*;
import static de.aservo.confapi.crowd.util.AttributeUtil.*;
import static java.lang.Boolean.TRUE;

public class DirectoryBeanUtil {

    public static final String ATTRIBUTE_USE_NESTED_GROUPS = "useNestedGroups";

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
        }

        return toDirectoryGenericBean(directory);
    }

    public static DirectoryInternalBean toDirectoryInternalBean(
            final Directory directory) {

        final DirectoryInternalBean directoryBean = new DirectoryInternalBean();
        directoryBean.setId(directory.getId());
        directoryBean.setName(directory.getName());
        directoryBean.setDescription(directory.getDescription());
        directoryBean.setActive(directory.isActive());
        directoryBean.setCreatedDate(directory.getCreatedDate());
        directoryBean.setUpdatedDate(directory.getUpdatedDate());

        final Map<String, String> attributes = new HashMap<>(directory.getAttributes());
        final Set<OperationType> allowedOperations = new HashSet<>(directory.getAllowedOperations());

        directoryBean.setCredentialPolicy(new DirectoryInternalBean.DirectoryInternalCredentialPolicy());
        directoryBean.getCredentialPolicy().setPasswordRegex(attributes.get(ATTRIBUTE_PASSWORD_REGEX));
        directoryBean.getCredentialPolicy().setPasswordComplexityMessage(attributes.get(ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE));
        directoryBean.getCredentialPolicy().setPasswordMaxAttempts(toLong(attributes.get(ATTRIBUTE_PASSWORD_MAX_ATTEMPTS)));
        directoryBean.getCredentialPolicy().setPasswordHistoryCount(toLong(attributes.get(ATTRIBUTE_PASSWORD_HISTORY_COUNT)));
        directoryBean.getCredentialPolicy().setPasswordMaxChangeTime(toLong(attributes.get(ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME)));
        directoryBean.getCredentialPolicy().setPasswordExpiryNotificationDays(toIntegerList(attributes.get(ATTRIBUTE_PASSWORD_EXPIRATION_NOTIFICATION_PERIODS)));
        directoryBean.getCredentialPolicy().setPasswordEncryptionMethod(attributes.get(ATTRIBUTE_USER_ENCRYPTION_METHOD));

        directoryBean.setAdvanced(new DirectoryInternalBean.DirectoryInternalAdvanced());
        directoryBean.getAdvanced().setEnableNestedGroups(toBoolean(attributes.getOrDefault(ATTRIBUTE_USE_NESTED_GROUPS, "false")));

        directoryBean.setPermissions(new DirectoryInternalBean.DirectoryInternalPermissions());
        directoryBean.getPermissions().setAddGroup(allowedOperations.contains(OperationType.CREATE_GROUP));
        directoryBean.getPermissions().setAddUser(allowedOperations.contains(OperationType.CREATE_USER));
        directoryBean.getPermissions().setModifyGroup(allowedOperations.contains(OperationType.UPDATE_GROUP));
        directoryBean.getPermissions().setModifyUser(allowedOperations.contains(OperationType.UPDATE_USER));
        directoryBean.getPermissions().setModifyGroupAttributes(allowedOperations.contains(OperationType.UPDATE_GROUP_ATTRIBUTE));
        directoryBean.getPermissions().setModifyUserAttributes(allowedOperations.contains(OperationType.UPDATE_USER_ATTRIBUTE));
        directoryBean.getPermissions().setRemoveGroup(allowedOperations.contains(OperationType.DELETE_GROUP));
        directoryBean.getPermissions().setRemoveUser(allowedOperations.contains(OperationType.DELETE_USER));

        return directoryBean;
    }

    private static DirectoryGenericBean toDirectoryGenericBean(
            final Directory directory) {

        final DirectoryGenericBean directoryBean = new DirectoryGenericBean();
        directoryBean.setId(directory.getId());
        directoryBean.setName(directory.getName());
        directoryBean.setDescription(directory.getDescription());
        directoryBean.setActive(directory.isActive());

        return directoryBean;
    }

    /**
     * Build directory.
     *
     * @param directoryBean the directory bean
     * @return the directory
     */
    @Nonnull
    public static Directory toDirectory(
            @Nonnull final AbstractDirectoryBean directoryBean) {

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
            @Nonnull final Directory directory) {

        final ImmutableDirectory.Builder directoryBuilder = ImmutableDirectory.builder(directory);

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

            final DirectoryInternalBean.DirectoryInternalCredentialPolicy credentialPolicy = directoryInternalBean.getCredentialPolicy();
            if (credentialPolicy != null) {
                setAttributeIfNotNull(attributes, ATTRIBUTE_PASSWORD_REGEX, credentialPolicy.getPasswordRegex());
                setAttributeIfNotNull(attributes, ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE, credentialPolicy.getPasswordComplexityMessage());
                setAttributeIfNotNull(attributes, ATTRIBUTE_PASSWORD_MAX_ATTEMPTS, fromLong(credentialPolicy.getPasswordMaxAttempts()));
                setAttributeIfNotNull(attributes, ATTRIBUTE_PASSWORD_HISTORY_COUNT, fromLong(credentialPolicy.getPasswordHistoryCount()));
                setAttributeIfNotNull(attributes, ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME, fromLong(credentialPolicy.getPasswordMaxChangeTime()));
                setAttributeIfNotNull(attributes, ATTRIBUTE_PASSWORD_EXPIRATION_NOTIFICATION_PERIODS, fromIntegerList(credentialPolicy.getPasswordExpiryNotificationDays()));
                setAttributeIfNotNull(attributes, ATTRIBUTE_USER_ENCRYPTION_METHOD, credentialPolicy.getPasswordEncryptionMethod());
            }

            final DirectoryInternalBean.DirectoryInternalAdvanced advanced = directoryInternalBean.getAdvanced();
            if (advanced != null) {
                setAttributeIfNotNull(attributes, ATTRIBUTE_USE_NESTED_GROUPS, fromBoolean(advanced.getEnableNestedGroups()));
            }

            final DirectoryInternalBean.DirectoryInternalPermissions permissions = directoryInternalBean.getPermissions();
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

        return directoryBuilder
                .setAttributes(attributes)
                .setAllowedOperations(allowedOperations)
                .build();
    }

    @Nonnull
    private static DirectoryType toDirectoryType(
            @Nonnull final AbstractDirectoryBean directoryBean) {

        if (DirectoryInternalBean.class.equals(directoryBean.getClass())) {
            return DirectoryType.INTERNAL;
        } else if (DirectoryCrowdBean.class.equals(directoryBean.getClass())) {
            return DirectoryType.CROWD;
        } else if (DirectoryLdapBean.class.equals(directoryBean.getClass())) {
            return DirectoryType.AZURE_AD;
        }

        return DirectoryType.UNKNOWN;
    }

    private static String toDirectoryImplClass(
            @Nonnull final AbstractDirectoryBean directoryBean) {

        if (DirectoryInternalBean.class.equals(directoryBean.getClass())) {
            return "com.atlassian.crowd.directory.InternalDirectory";
        }

        return null;
    }

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

    private DirectoryBeanUtil() {
    }

}
