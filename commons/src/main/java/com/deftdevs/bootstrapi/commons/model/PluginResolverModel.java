package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.model.type.PluginResolverType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.UPM_RESOLVER;

/**
 * A named plugin resolver: its type (the Atlassian Marketplace or a Maven
 * repository) and the endpoint it works against, either directly or through
 * a proxying repository (e.g. an Artifactory generic remote). Credentials
 * authenticate against the endpoint itself; an optional web proxy (with its
 * own credentials) covers endpoints only reachable through a forward proxy.
 * Plugins reference a resolver by its key in the {@code resolvers} map.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = UPM_RESOLVER)
public class PluginResolverModel {

    @XmlElement
    private PluginResolverType type;

    @XmlElement
    private String baseUrl;

    @XmlElement
    private String username;

    @XmlElement
    private String password;

    @XmlElement
    private PluginProxyModel proxy;

}
