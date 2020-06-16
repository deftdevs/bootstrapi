package de.aservo.confapi.crowd.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;

import static com.atlassian.crowd.password.factory.PasswordEncoderFactory.ATLASSIAN_SECURITY_ENCODER;

@Data
@NoArgsConstructor
@XmlRootElement(name = "configuration")
public class DirectoryConfigurationBean {

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

    @XmlElement
    private List<Integer> passwordExpiryNotificationDays;

    @XmlElement
    private String passwordEncryptionMethod;

    // examples

    public static final DirectoryConfigurationBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new DirectoryConfigurationBean();
        EXAMPLE_1.setPasswordRegex("[a-zA-Z0-9]+");
        EXAMPLE_1.setPasswordComplexityMessage("Only alphanumeric characters");
        EXAMPLE_1.setPasswordMaxAttempts(3L);
        EXAMPLE_1.setPasswordHistoryCount(10L);
        EXAMPLE_1.setPasswordMaxChangeTime(60L);
        EXAMPLE_1.setPasswordExpiryNotificationDays(Arrays.asList(1, 7));
        EXAMPLE_1.setPasswordEncryptionMethod(ATLASSIAN_SECURITY_ENCODER);
    }

}
