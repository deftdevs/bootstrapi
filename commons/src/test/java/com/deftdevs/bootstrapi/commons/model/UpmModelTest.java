package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.model.type.PluginResolverType;
import com.deftdevs.bootstrapi.commons.rest.provider.YamlObjectMapperHolder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpmModelTest {

    @Test
    void testYamlBindsTheKeyedMapsAndLowercaseTypes() throws IOException {
        final String yaml = "resolvers:\n"
                + "  central:\n"
                + "    type: marketplace\n"
                + "    baseUrl: https://marketplace.example.com\n"
                + "  corp:\n"
                + "    type: maven\n"
                + "    baseUrl: https://repository.example.com/maven\n"
                + "    proxy:\n"
                + "      host: proxy.example.com\n"
                + "      port: 3128\n"
                + "plugins:\n"
                + "  com.example.plugin:\n"
                + "    version: 1.2.3\n"
                + "    resolver: central\n"
                + "  com.example.other:\n"
                + "    version: 2.0.0\n"
                + "    resolver: corp\n"
                + "    groupId: com.example\n"
                + "    artifactId: other-plugin\n";

        final UpmModel model = YamlObjectMapperHolder.YAML_OBJECT_MAPPER.readValue(yaml, UpmModel.class);

        assertEquals(PluginResolverType.MARKETPLACE, model.getResolvers().get("central").getType());
        assertEquals(PluginResolverType.MAVEN, model.getResolvers().get("corp").getType());
        assertEquals("proxy.example.com", model.getResolvers().get("corp").getProxy().getHost());
        assertEquals("central", model.getPlugins().get("com.example.plugin").getResolver());
        assertEquals("2.0.0", model.getPlugins().get("com.example.other").getVersion());
    }

    @Test
    void testYamlWritesTheLowercaseTypeNames() throws IOException {
        final UpmModel model = UpmModel.builder()
                .resolvers(Map.of("central", PluginResolverModel.builder()
                        .type(PluginResolverType.MARKETPLACE)
                        .baseUrl("https://marketplace.example.com")
                        .build()))
                .build();

        final String yaml = YamlObjectMapperHolder.YAML_OBJECT_MAPPER.writeValueAsString(model);

        assertTrue(yaml.contains("type: \"marketplace\"") || yaml.contains("type: marketplace"),
                "the resolver type must serialize as its lowercase name, got:\n" + yaml);
    }
}
