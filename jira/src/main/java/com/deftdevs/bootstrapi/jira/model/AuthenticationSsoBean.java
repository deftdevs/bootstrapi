package com.deftdevs.bootstrapi.jira.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = BootstrAPI.AUTHENTICATION + "-" + BootstrAPI.AUTHENTICATION_SSO)
public class AuthenticationSsoBean extends com.deftdevs.bootstrapi.commons.model.AuthenticationSsoBean {

    @XmlElement
    private Boolean showOnLoginForJsm;

    // Example instances for documentation and tests

    public static final AuthenticationSsoBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new AuthenticationSsoBean();
        EXAMPLE_1.setShowOnLogin(true);
        EXAMPLE_1.setShowOnLoginForJsm(true);
        EXAMPLE_1.setEnableAuthenticationFallback(true);
    }

}
