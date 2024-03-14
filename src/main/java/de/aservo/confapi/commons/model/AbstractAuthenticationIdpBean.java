package de.aservo.confapi.commons.model;

import de.aservo.confapi.commons.constants.ConfAPI;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlRootElement
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
// Note: New subtypes must also be registered in AuthenticationIdpsBean.java to be considered in the REST API documentation
@JsonSubTypes({
        @JsonSubTypes.Type(value = AuthenticationIdpOidcBean.class, name = ConfAPI.AUTHENTICATION_IDP_OIDC),
        @JsonSubTypes.Type(value = AuthenticationIdpSamlBean.class, name = ConfAPI.AUTHENTICATION_IDP_SAML),
})
public abstract class AbstractAuthenticationIdpBean {

    @XmlElement
    private Long id;

    @XmlElement
    private String name;

    @XmlElement
    private Boolean enabled;

    @XmlElement
    private String url;

    @XmlElement
    private Boolean enableRememberMe;

    @XmlElement
    private String buttonText;

}
