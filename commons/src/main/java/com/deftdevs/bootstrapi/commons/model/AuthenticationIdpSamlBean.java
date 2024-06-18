package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = BootstrAPI.AUTHENTICATION + "-" + BootstrAPI.AUTHENTICATION_IDP + "-" + BootstrAPI.AUTHENTICATION_IDP_SAML)
public class AuthenticationIdpSamlBean extends AbstractAuthenticationIdpBean {

    @XmlElement
    private String certificate;

    @XmlElement
    private String usernameAttribute;

    // Just-in-time user provisioning is not implemented yet

    // Example instances for documentation and tests

    public static final AuthenticationIdpSamlBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new AuthenticationIdpSamlBean();
        EXAMPLE_1.setId(1L);
        EXAMPLE_1.setName("SAML");
        EXAMPLE_1.setEnabled(true);
        EXAMPLE_1.setUrl("https://saml.example.com");
        EXAMPLE_1.setEnableRememberMe(true);
        EXAMPLE_1.setButtonText("Login with SAML");
        EXAMPLE_1.setCertificate("certificate");
        EXAMPLE_1.setUsernameAttribute("username");
    }

}
