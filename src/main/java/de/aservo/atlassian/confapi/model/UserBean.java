package de.aservo.atlassian.confapi.model;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean for users REST requests.
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = ConfAPI.USER)
public class UserBean {

    @XmlElement
    private String userName;

    @XmlElement
    private String fullName;

    @XmlElement
    private String email;

    @XmlElement
    @EqualsAndHashCode.Exclude
    private String password;

    // Example instances for documentation and tests

    public static final UserBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new UserBean();
        EXAMPLE_1.setUserName("example");
        EXAMPLE_1.setFullName("Example User");
        EXAMPLE_1.setEmail("user@example.com");
        EXAMPLE_1.setPassword("3x4mpl3");
    }

    public static final UserBean EXAMPLE_2;

    static {
        EXAMPLE_2 = new UserBean();
        EXAMPLE_2.setUserName("example");
        EXAMPLE_2.setFullName("Changed Example User");
        EXAMPLE_2.setEmail("user@new-example.com");
    }

}
