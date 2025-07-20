package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = BootstrAPI.AUTHENTICATION + "-" + BootstrAPI.AUTHENTICATION_IDP + "-" + BootstrAPI.AUTHENTICATION_IDP_SAML)
public class AuthenticationIdpSamlModel extends AbstractAuthenticationIdpModel {

    @XmlElement
    private String certificate;

    @XmlElement
    private String usernameAttribute;

    // Just-in-time user provisioning is not implemented yet

    // Example instances for documentation and tests

    public static final AuthenticationIdpSamlModel EXAMPLE_1 = AuthenticationIdpSamlModel.builder()
            .id(1L)
            .name("SAML")
            .enabled(true)
            .url("https://saml.example.com")
            .enableRememberMe(true)
            .buttonText("Login with SAML")
            .certificate("certificate")
            .usernameAttribute("username")
            .build();

}
