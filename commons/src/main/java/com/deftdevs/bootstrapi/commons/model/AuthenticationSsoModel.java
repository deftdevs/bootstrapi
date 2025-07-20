package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@XmlRootElement(name = BootstrAPI.AUTHENTICATION + "-" + BootstrAPI.AUTHENTICATION_SSO)
public class AuthenticationSsoModel {

    @XmlElement
    private Boolean showOnLogin;

    @XmlElement
    private Boolean showOnLoginForJsm;

    @XmlElement
    private Boolean enableAuthenticationFallback;

    // Example instances for documentation and tests

    public static final AuthenticationSsoModel EXAMPLE_1 = AuthenticationSsoModel.builder()
            .showOnLogin(true)
            .showOnLoginForJsm(true)
            .enableAuthenticationFallback(true)
            .build();

}
