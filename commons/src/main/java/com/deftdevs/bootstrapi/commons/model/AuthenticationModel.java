package com.deftdevs.bootstrapi.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.AUTHENTICATION;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = AUTHENTICATION)
public class AuthenticationModel {

    @XmlElement
    private Map<String, AbstractAuthenticationIdpModel> idps;

    @XmlElement
    private AuthenticationSsoModel sso;

}
