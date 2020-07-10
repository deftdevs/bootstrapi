package de.aservo.confapi.crowd.model.util;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.model.directory.ImmutableDirectory;
import de.aservo.confapi.commons.model.DirectoryInternalBean;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.atlassian.crowd.directory.AbstractInternalDirectory.*;

public class DirectoryInternalBeanUtil {

    public static final String LIST_SEPARATOR = ",";

    /**
     * Build directory bean.
     *
     * @param directory the directory
     * @return the directory bean
     */
    @Nullable
    public static DirectoryInternalBean toDirectoryInternalBean(
            @Nullable final Directory directory) {

        if (directory == null) {
            return null;
        }

        final DirectoryInternalBean directoryBean = new DirectoryInternalBean();
        directoryBean.setId(directory.getId());
        directoryBean.setName(directory.getName());
        directoryBean.setDescription(directory.getDescription());
        directoryBean.setActive(directory.isActive());

        final Map<String, String> directoryAttributes = directory.getAttributes();
        final DirectoryInternalBean.DirectoryInternalCredentialPolicy credentialPolicy = new DirectoryInternalBean.DirectoryInternalCredentialPolicy();
        credentialPolicy.setPasswordRegex(directoryAttributes.get(ATTRIBUTE_PASSWORD_REGEX));
        credentialPolicy.setPasswordComplexityMessage(directoryAttributes.get(ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE));
        credentialPolicy.setPasswordMaxAttempts(toLong(directoryAttributes.get(ATTRIBUTE_PASSWORD_MAX_ATTEMPTS)));
        credentialPolicy.setPasswordHistoryCount(toLong(directoryAttributes.get(ATTRIBUTE_PASSWORD_HISTORY_COUNT)));
        credentialPolicy.setPasswordMaxChangeTime(toLong(directoryAttributes.get(ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME)));
        credentialPolicy.setPasswordExpiryNotificationDays(toList(directoryAttributes.get(ATTRIBUTE_PASSWORD_EXPIRATION_NOTIFICATION_PERIODS)));
        credentialPolicy.setPasswordEncryptionMethod(directoryAttributes.get(ATTRIBUTE_USER_ENCRYPTION_METHOD));

        directoryBean.setCredentialPolicy(credentialPolicy);
        return directoryBean;
    }

    /**
     * Build directory.
     *
     * @param directoryInternalBean the directory bean
     * @return the directory
     */
    @Nullable
    public static Directory toDirectory(
            @Nullable final DirectoryInternalBean directoryInternalBean) {

        if (directoryInternalBean == null) {
            return null;
        }

        final Map<String, String> directoryAttributes = new HashMap<>();
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

        return ImmutableDirectory.builder(directoryInternalBean.getName(), DirectoryType.INTERNAL, "")
                .setId(directoryInternalBean.getId())
                .setDescription(directoryInternalBean.getDescription())
                .setActive(directoryInternalBean.isActive())
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

    private DirectoryInternalBeanUtil() {
    }

}
