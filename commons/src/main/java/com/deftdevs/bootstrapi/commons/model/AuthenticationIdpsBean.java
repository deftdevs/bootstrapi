package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = ConfAPI.AUTHENTICATION + "-" + ConfAPI.AUTHENTICATION_IDPS)
public class AuthenticationIdpsBean {

    @XmlElement
    @Schema(anyOf = {
            AuthenticationIdpOidcBean.class,
    })
    private Collection<AbstractAuthenticationIdpBean> authenticationIdpBeans;

}
