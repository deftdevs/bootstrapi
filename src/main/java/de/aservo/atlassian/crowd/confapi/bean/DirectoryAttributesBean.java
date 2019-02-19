package de.aservo.atlassian.crowd.confapi.bean;

import com.atlassian.crowd.embedded.api.Directory;
import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.atlassian.crowd.directory.AbstractInternalDirectory.*;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * Bean for directory attributes in REST queries.
 */
@XmlRootElement(name = "attributes")
public class DirectoryAttributesBean {

    @XmlElement
    private final String passwordRegex;

    @XmlElement
    private final String passwordComplexityMessage;

    @XmlElement
    private final Long passwordMaxAttempts;

    @XmlElement
    private final Long passwordHistoryCount;

    @XmlElement
    private final Long passwordMaxChangeTime;

    /**
     * The default constructor is needed for JSON request deserialization.
     */
    public DirectoryAttributesBean() {
        this.passwordRegex = null;
        this.passwordComplexityMessage = null;
        this.passwordMaxAttempts = null;
        this.passwordHistoryCount = null;
        this.passwordMaxChangeTime = null;
    }

    public DirectoryAttributesBean(
            final Directory directory) {

        this.passwordRegex = extractPasswordRegex(directory);
        this.passwordComplexityMessage = extractPasswordComplexityMessage(directory);
        this.passwordMaxAttempts = extractPasswordMaxAttempts(directory);
        this.passwordHistoryCount = extractPasswordHistoryCount(directory);
        this.passwordMaxChangeTime = extractPasswordMaxChangeTimeAttribute(directory);
    }

    public String getPasswordRegex() {
        return passwordRegex;
    }

    public String getPasswordComplexityMessage() {
        return passwordComplexityMessage;
    }

    public Long getPasswordMaxAttempts() {
        return passwordMaxAttempts;
    }

    public Long getPasswordHistoryCount() {
        return passwordHistoryCount;
    }

    public Long getPasswordMaxChangeTime() {
        return passwordMaxChangeTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) { return false; }

        final DirectoryAttributesBean directoryAttributesBean = (DirectoryAttributesBean) obj;
        return new EqualsBuilder()
                .append(passwordRegex, directoryAttributesBean.passwordRegex)
                .append(passwordComplexityMessage, directoryAttributesBean.passwordComplexityMessage)
                .append(passwordMaxAttempts, directoryAttributesBean.passwordMaxAttempts)
                .append(passwordHistoryCount, directoryAttributesBean.passwordHistoryCount)
                .append(passwordMaxChangeTime, directoryAttributesBean.passwordMaxChangeTime)
                .isEquals();
    }

    private static String extractPasswordRegex(
            final Directory directory) {

        return directory.getValue(ATTRIBUTE_PASSWORD_REGEX);
    }

    private static String extractPasswordComplexityMessage(
            final Directory directory) {

        return directory.getValue(ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE);
    }

    private static Long extractPasswordMaxAttempts(
            final Directory directory) {

        return extractLongAttribute(directory, ATTRIBUTE_PASSWORD_MAX_ATTEMPTS);
    }

    private static Long extractPasswordHistoryCount(
            final Directory directory) {

        return extractLongAttribute(directory, ATTRIBUTE_PASSWORD_HISTORY_COUNT);
    }

    private static Long extractPasswordMaxChangeTimeAttribute(
            final Directory directory) {

        return extractLongAttribute(directory, ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME);
    }

    private static Long extractLongAttribute(
            final Directory directory,
            final String attributeName) {

        final String attributeValue = directory.getValue(attributeName);

        if (isNotBlank(attributeValue)) {
            return Long.valueOf(attributeValue);
        }

        return null;
    }

}
