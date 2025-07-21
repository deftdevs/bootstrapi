package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = BootstrAPI.AUTHENTICATION + "-" + BootstrAPI.AUTHENTICATION_IDP + "-" + BootstrAPI.AUTHENTICATION_IDP_OIDC)
public class AuthenticationIdpOidcModel extends AbstractAuthenticationIdpModel {

    @XmlElement
    private String clientId;

    @XmlElement
    private String clientSecret;

    @XmlElement
    private String usernameClaim;

    @XmlElement
    private List<String> additionalScopes;

    @XmlElement
    private Boolean discoveryEnabled;

    @XmlElement
    private String authorizationEndpoint;

    @XmlElement
    private String tokenEndpoint;

    @XmlElement
    private String userInfoEndpoint;

    // Just-in-time user provisioning is not implemented yet

    // Example instances for documentation and tests

    public static final AuthenticationIdpOidcModel EXAMPLE_1 = AuthenticationIdpOidcModel.builder()
            .id(1L)
            .name("OIDC")
            .enabled(true)
            .url("https://oidc.example.com")
            .enableRememberMe(true)
            .buttonText("Login with OIDC")
            .clientId("oidc")
            .clientSecret("s3cr3t")
            .usernameClaim("userName")
            .additionalScopes(Collections.emptyList())
            .discoveryEnabled(false)
            .authorizationEndpoint("https://oidc.example.com/authorization")
            .tokenEndpoint("https://oidc.example.com/token")
            .userInfoEndpoint("https://oidc.example.com/userinfo")
            .build();

}
