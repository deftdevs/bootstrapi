package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Map;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.UPM;

/**
 * Declarative plugin installation: the named resolvers and the plugins to
 * install, both keyed maps so that merged YAML documents can override
 * single values. Responses echo only the plugins map, never the resolver
 * credentials.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = UPM)
public class UpmModel {

    @XmlElement
    private Map<String, PluginResolverModel> resolvers;

    @XmlElement
    private Map<String, PluginModel> plugins;

    @XmlElement
    private Map<String, _AllModelStatus> status;

}
