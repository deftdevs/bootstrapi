package com.deftdevs.bootstrapi.commons.rest.provider;

import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class YamlMessageBodyWriterTest {

    private final YamlMessageBodyWriter writer = new YamlMessageBodyWriter();

    @Test
    void testIsWriteable() {
        assertTrue(writer.isWriteable(SettingsGeneralModel.class, SettingsGeneralModel.class, null, null));
    }

    @Test
    void testGetSizeIsUnknown() {
        assertEquals(-1, writer.getSize(null, null, null, null, null));
    }

    @Test
    void testWritesModelAsYaml() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        writer.writeTo(SettingsGeneralModel.EXAMPLE_1_MINIMAL, SettingsGeneralModel.class,
                SettingsGeneralModel.class, null, null, null, outputStream);

        final String yaml = outputStream.toString(StandardCharsets.UTF_8.name());
        assertTrue(yaml.contains("title: \"" + SettingsGeneralModel.EXAMPLE_1_MINIMAL.getTitle() + "\"")
                        || yaml.contains("title: " + SettingsGeneralModel.EXAMPLE_1_MINIMAL.getTitle()),
                "expected the title in the YAML output, got:\n" + yaml);
    }
}
