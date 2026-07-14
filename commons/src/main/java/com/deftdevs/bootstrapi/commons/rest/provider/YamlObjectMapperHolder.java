package com.deftdevs.bootstrapi.commons.rest.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * The shared YAML {@link ObjectMapper} of the YAML message body providers
 * and the startup configuration import. The models are bound through their
 * Jackson annotations and plain field names, exactly like the JSON providers
 * bind them, so no further modules are required (and none must be:
 * provided-scope Jackson modules are not visible to the plugin bundle at
 * runtime).
 */
public final class YamlObjectMapperHolder {

    public static final ObjectMapper YAML_OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

    private YamlObjectMapperHolder() {
    }
}
