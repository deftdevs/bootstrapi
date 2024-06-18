package de.aservo.confapi.commons.model;

import de.aservo.confapi.commons.constants.ConfAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlRootElement(name = ConfAPI.AUTHENTICATION + "-" + ConfAPI.AUTHENTICATION_SSO)
public class AuthenticationSsoBean {

    @XmlElement
    private Boolean showOnLogin;

    // Example instances for documentation and tests

    public static final AuthenticationSsoBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new AuthenticationSsoBean();
        EXAMPLE_1.setShowOnLogin(true);
    }

}
