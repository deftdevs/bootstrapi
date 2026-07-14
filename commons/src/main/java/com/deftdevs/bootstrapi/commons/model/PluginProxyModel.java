package com.deftdevs.bootstrapi.commons.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.UPM_PROXY;

/**
 * An optional web proxy for a plugin resolver, for endpoints that are only
 * reachable through a forward proxy.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = UPM_PROXY)
public class PluginProxyModel {

    @XmlElement
    private String host;

    @XmlElement
    private Integer port;

    @XmlElement
    private String username;

    @XmlElement
    private String password;

}
