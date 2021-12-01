package de.aservo.confapi.crowd.model.util;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.model.directory.ImmutableDirectory;
import de.aservo.confapi.commons.model.AbstractDirectoryBean;
import de.aservo.confapi.commons.model.DirectoryGenericBean;
import de.aservo.confapi.commons.model.DirectoryInternalBean;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.atlassian.crowd.directory.AbstractInternalDirectory.*;

public class DirectoryBeanUtil {

    public static final String LIST_SEPARATOR = ",";

    /**
     * Build directory bean.
     *
     * @param directory the directory
     * @return the directory bean
     */
    @Nullable
    public static AbstractDirectoryBean toDirectoryBean(
            @Nullable final Directory directory) {

        if (directory == null) {
            return null;
        }

        if (directory.getType().equals(DirectoryType.INTERNAL)) {
            return toDirectoryInternalBean(directory);
        }

        return toDirectoryGenericBean(directory);
    }

    private static DirectoryInternalBean toDirectoryInternalBean(
            final Directory directory) {

        final DirectoryInternalBean directoryBean = new DirectoryInternalBean();
        directoryBean.setId(directory.getId());
        directoryBean.setName(directory.getName());
        directoryBean.setDescription(directory.getDescription());
        directoryBean.setActive(directory.isActive());

        final Map<String, String> directoryAttributes = directory.getAttributes();
        directoryBean.setCredentialPolicy(new DirectoryInternalBean.DirectoryInternalCredentialPolicy());
        directoryBean.getCredentialPolicy().setPasswordRegex(directoryAttributes.get(ATTRIBUTE_PASSWORD_REGEX));
        directoryBean.getCredentialPolicy().setPasswordComplexityMessage(directoryAttributes.get(ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE));
        directoryBean.getCredentialPolicy().setPasswordMaxAttempts(toLong(directoryAttributes.get(ATTRIBUTE_PASSWORD_MAX_ATTEMPTS)));
        directoryBean.getCredentialPolicy().setPasswordHistoryCount(toLong(directoryAttributes.get(ATTRIBUTE_PASSWORD_HISTORY_COUNT)));
        directoryBean.getCredentialPolicy().setPasswordMaxChangeTime(toLong(directoryAttributes.get(ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME)));
        directoryBean.getCredentialPolicy().setPasswordExpiryNotificationDays(toList(directoryAttributes.get(ATTRIBUTE_PASSWORD_EXPIRATION_NOTIFICATION_PERIODS)));
        directoryBean.getCredentialPolicy().setPasswordEncryptionMethod(directoryAttributes.get(ATTRIBUTE_USER_ENCRYPTION_METHOD));

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
    @Nullable
    public static Directory toDirectory(
            @Nullable final AbstractDirectoryBean directoryBean) {

        if (directoryBean == null) {
            return null;
        }

        DirectoryType directoryType = DirectoryType.UNKNOWN;
        final Map<String, String> directoryAttributes = new HashMap<>();

        if (directoryBean instanceof DirectoryInternalBean) {
            directoryType = DirectoryType.INTERNAL;
            final DirectoryInternalBean directoryInternalBean = (DirectoryInternalBean) directoryBean;
            final DirectoryInternalBean.DirectoryInternalCredentialPolicy credentialPolicy = directoryInternalBean.getCredentialPolicy();

            if (credentialPolicy != null) {
                directoryAttributes.put(ATTRIBUTE_PASSWORD_REGEX, credentialPolicy.getPasswordRegex());
                directoryAttributes.put(ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE, credentialPolicy.getPasswordComplexityMessage());
                directoryAttributes.put(ATTRIBUTE_PASSWORD_MAX_ATTEMPTS, fromLong(credentialPolicy.getPasswordMaxAttempts()));
                directoryAttributes.put(ATTRIBUTE_PASSWORD_HISTORY_COUNT, fromLong(credentialPolicy.getPasswordHistoryCount()));
                directoryAttributes.put(ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME, fromLong(credentialPolicy.getPasswordMaxChangeTime()));
                directoryAttributes.put(ATTRIBUTE_PASSWORD_EXPIRATION_NOTIFICATION_PERIODS, fromList(credentialPolicy.getPasswordExpiryNotificationDays()));
                directoryAttributes.put(ATTRIBUTE_USER_ENCRYPTION_METHOD, credentialPolicy.getPasswordEncryptionMethod());
            }
        }

        return ImmutableDirectory.builder(directoryBean.getName(), directoryType, "")
                .setId(directoryBean.getId())
                .setDescription(directoryBean.getDescription())
                .setActive(directoryBean.getActive())
                .setAttributes(directoryAttributes)
                .build();
    }

    private static String fromList(
            @Nullable final List<Integer> value) {

        if (value == null) {
            return null;
        }

        return value.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(LIST_SEPARATOR));
    }

    @Nullable
    private static String fromLong(
            @Nullable final Long value) {

        if (value == null) {
            return null;
        }

        return String.valueOf(value);
    }

    @Nullable
    private static List<Integer> toList(
            @Nullable final String value) {

        if (value == null) {
            return null;
        }

        return Stream.of(value.split(LIST_SEPARATOR))
                .sorted()
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    @Nullable
    private static Long toLong(
            @Nullable final String value) {

        if (value == null) {
            return null;
        }

        return Long.valueOf(value);
    }

    private DirectoryBeanUtil() {
    }

}
