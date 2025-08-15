package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;




@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AuthenticationIdpOidcModel.class, name = BootstrAPI.AUTHENTICATION_IDP_OIDC),
        @JsonSubTypes.Type(value = AuthenticationIdpSamlModel.class, name = BootstrAPI.AUTHENTICATION_IDP_SAML),
})
// Note: The subtypes must also be registered for swagger to be considered in the REST API documentation
@Schema(anyOf = {
        AuthenticationIdpOidcModel.class,
        AuthenticationIdpSamlModel.class,
})
public abstract class AbstractAuthenticationIdpModel {

    @XmlElement
    private Long id;

    @XmlElement
    private String name;

    @XmlElement
    private Boolean enabled;

    @XmlElement
    private Boolean enabledForJsm;

    @XmlElement
    private String url;

    @XmlElement
    private Boolean enableRememberMe;

    @XmlElement
    private String buttonText;

}
