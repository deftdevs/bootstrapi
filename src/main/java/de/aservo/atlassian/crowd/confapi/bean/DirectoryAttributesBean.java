package de.aservo.atlassian.crowd.confapi.bean;

import com.atlassian.crowd.embedded.api.Directory;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.atlassian.crowd.directory.AbstractInternalDirectory.*;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@Data
@NoArgsConstructor
@XmlRootElement(name = "attributes")
public class DirectoryAttributesBean {

    @XmlElement
    private String passwordRegex;

    @XmlElement
    private String passwordComplexityMessage;

    @XmlElement
    private Long passwordMaxAttempts;

    @XmlElement
    private Long passwordHistoryCount;

    @XmlElement
    private Long passwordMaxChangeTime;

    public static DirectoryAttributesBean from(
            final Directory directory) {

        final DirectoryAttributesBean directoryAttributesBean = new DirectoryAttributesBean();
        directoryAttributesBean.setPasswordRegex(extractPasswordRegex(directory));
        directoryAttributesBean.setPasswordComplexityMessage(extractPasswordComplexityMessage(directory));
        directoryAttributesBean.setPasswordMaxAttempts(extractPasswordMaxAttempts(directory));
        directoryAttributesBean.setPasswordHistoryCount(extractPasswordHistoryCount(directory));
        directoryAttributesBean.setPasswordMaxChangeTime(extractPasswordMaxChangeTimeAttribute(directory));
        return directoryAttributesBean;
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

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

}
