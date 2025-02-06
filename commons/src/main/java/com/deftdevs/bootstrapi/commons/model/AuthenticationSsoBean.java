package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlRootElement(name = BootstrAPI.AUTHENTICATION + "-" + BootstrAPI.AUTHENTICATION_SSO)
public class AuthenticationSsoBean {

    @XmlElement
    private Boolean showOnLogin;

    @XmlElement
    private Boolean showOnLoginForJsm;

    @XmlElement
    private Boolean enableAuthenticationFallback;

    // Example instances for documentation and tests

    public static final AuthenticationSsoBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new AuthenticationSsoBean();
        EXAMPLE_1.setShowOnLogin(true);
        EXAMPLE_1.setShowOnLoginForJsm(true);
        EXAMPLE_1.setEnableAuthenticationFallback(true);
    }

}
