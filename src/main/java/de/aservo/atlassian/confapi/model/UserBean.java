package de.aservo.atlassian.confapi.model;

import com.atlassian.user.User;
import de.aservo.atlassian.confapi.constants.ConfAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotNull
    @Size(min = 1)
    private String userName;

    @XmlElement
    private String fullName;

    @XmlElement
    private String email;

    @XmlElement
    private String password;

    /**
     * Instantiates a new user bean.
     *
     * @param user the user
     */
    public UserBean(User user) {
        userName = user.getName();
        fullName = user.getFullName();
        email = user.getEmail();
    }

}
