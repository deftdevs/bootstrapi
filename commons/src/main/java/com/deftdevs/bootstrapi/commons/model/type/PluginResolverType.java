package com.deftdevs.bootstrapi.commons.model.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The kind of artifact source a named plugin resolver works against.
 */
public enum PluginResolverType {

    @JsonProperty("marketplace")
    MARKETPLACE,

    @JsonProperty("maven")
    MAVEN,

}
