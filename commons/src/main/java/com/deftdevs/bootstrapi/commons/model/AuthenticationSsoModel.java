package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlElement;
import com.deftdevs.bootstrapi.commons.model.type.SubEntityOf;
import jakarta.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SubEntityOf(AuthenticationModel.class)
@XmlRootElement(name = BootstrAPI.AUTHENTICATION_SSO)
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
