package de.aservo.confapi.crowd.model.util;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.model.directory.ImmutableDirectory;
import de.aservo.confapi.crowd.model.DirectoryBean;
import de.aservo.confapi.crowd.model.DirectoryConfigurationBean;

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
    public static DirectoryBean toDirectoryBean(
            @Nullable final Directory directory) {

        if (directory == null) {
            return null;
        }

        final DirectoryBean directoryBean = new DirectoryBean();
        directoryBean.setId(directory.getId());
        directoryBean.setName(directory.getName());
        directoryBean.setDescription(directory.getDescription());
        directoryBean.setActive(directory.isActive());

        final Map<String, String> directoryAttributes = directory.getAttributes();
        final DirectoryConfigurationBean directoryConfigurationBean = new DirectoryConfigurationBean();
        directoryConfigurationBean.setPasswordRegex(directoryAttributes.get(ATTRIBUTE_PASSWORD_REGEX));
        directoryConfigurationBean.setPasswordComplexityMessage(directoryAttributes.get(ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE));
        directoryConfigurationBean.setPasswordMaxAttempts(toLong(directoryAttributes.get(ATTRIBUTE_PASSWORD_MAX_ATTEMPTS)));
        directoryConfigurationBean.setPasswordHistoryCount(toLong(directoryAttributes.get(ATTRIBUTE_PASSWORD_HISTORY_COUNT)));
        directoryConfigurationBean.setPasswordMaxChangeTime(toLong(directoryAttributes.get(ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME)));
        directoryConfigurationBean.setPasswordExpiryNotificationDays(toList(directoryAttributes.get(ATTRIBUTE_PASSWORD_EXPIRATION_NOTIFICATION_PERIODS)));
        directoryConfigurationBean.setPasswordEncryptionMethod(directoryAttributes.get(ATTRIBUTE_USER_ENCRYPTION_METHOD));

        directoryBean.setConfiguration(directoryConfigurationBean);
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
            @Nullable final DirectoryBean directoryBean) {

        if (directoryBean == null) {
            return null;
        }

        final Map<String, String> directoryAttributes = new HashMap<>();
        final DirectoryConfigurationBean directoryConfigurationBean = directoryBean.getConfiguration();

        if (directoryConfigurationBean != null) {
            directoryAttributes.put(ATTRIBUTE_PASSWORD_REGEX, directoryConfigurationBean.getPasswordRegex());
            directoryAttributes.put(ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE, directoryConfigurationBean.getPasswordComplexityMessage());
            directoryAttributes.put(ATTRIBUTE_PASSWORD_MAX_ATTEMPTS, fromLong(directoryConfigurationBean.getPasswordMaxAttempts()));
            directoryAttributes.put(ATTRIBUTE_PASSWORD_HISTORY_COUNT, fromLong(directoryConfigurationBean.getPasswordHistoryCount()));
            directoryAttributes.put(ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME, fromLong(directoryConfigurationBean.getPasswordMaxChangeTime()));
            directoryAttributes.put(ATTRIBUTE_PASSWORD_EXPIRATION_NOTIFICATION_PERIODS, fromList(directoryConfigurationBean.getPasswordExpiryNotificationDays()));
            directoryAttributes.put(ATTRIBUTE_USER_ENCRYPTION_METHOD, directoryConfigurationBean.getPasswordEncryptionMethod());
        }

        return ImmutableDirectory.builder(directoryBean.getName(), DirectoryType.INTERNAL, "")
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
