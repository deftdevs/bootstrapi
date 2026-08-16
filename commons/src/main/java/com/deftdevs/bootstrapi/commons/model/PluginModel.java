package com.deftdevs.bootstrapi.commons.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.PLUGIN;

/**
 * One plugin to install, keyed by its plugin key in the {@code plugins}
 * map: the version plus a reference to the named resolver the artifact is
 * fetched from. Marketplace-type resolvers work from the plugin key and
 * version alone; Maven-type resolvers additionally need the entry's Maven
 * coordinates.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = PLUGIN)
public class PluginModel {

    @XmlElement
    private String version;

    /** The key of the resolver in the {@code resolvers} map. */
    @XmlElement
    private String resolver;

    @XmlElement
    private String groupId;

    @XmlElement
    private String artifactId;

    /** Defaults to enabled when absent. */
    @XmlElement
    private Boolean enabled;

}
