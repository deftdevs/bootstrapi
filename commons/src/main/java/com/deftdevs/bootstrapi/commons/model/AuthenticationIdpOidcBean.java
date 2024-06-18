package de.aservo.confapi.commons.model;

import de.aservo.confapi.commons.constants.ConfAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = ConfAPI.AUTHENTICATION + "-" + ConfAPI.AUTHENTICATION_IDP + "-" + ConfAPI.AUTHENTICATION_IDP_OIDC)
public class AuthenticationIdpOidcBean extends AbstractAuthenticationIdpBean {

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

    public static final AuthenticationIdpOidcBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new AuthenticationIdpOidcBean();
        EXAMPLE_1.setId(1L);
        EXAMPLE_1.setName("OIDC");
        EXAMPLE_1.setEnabled(true);
        EXAMPLE_1.setUrl("https://oidc.example.com");
        EXAMPLE_1.setEnableRememberMe(true);
        EXAMPLE_1.setButtonText("Login with OIDC");
        EXAMPLE_1.setClientId("oidc");
        EXAMPLE_1.setClientSecret("s3cr3t");
        EXAMPLE_1.setUsernameClaim("userName");
        EXAMPLE_1.setAdditionalScopes(Collections.emptyList());
        EXAMPLE_1.setDiscoveryEnabled(false);
        EXAMPLE_1.setAuthorizationEndpoint("https://oidc.example.com/authorization");
        EXAMPLE_1.setTokenEndpoint("https://oidc.example.com/token");
        EXAMPLE_1.setUserInfoEndpoint("https://oidc.example.com/userinfo");
    }

}
