package com.deftdevs.bootstrapi.commons.rest.provider;

import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryInternalModel;
import com.deftdevs.bootstrapi.commons.model.MailServerModel;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class YamlMessageBodyReaderTest {

    private final YamlMessageBodyReader reader = new YamlMessageBodyReader();

    @Test
    void testIsReadable() {
        assertTrue(reader.isReadable(SettingsGeneralModel.class, SettingsGeneralModel.class, null, null));
    }

    @Test
    void testReadsModelFromYaml() throws Exception {
        final SettingsGeneralModel model = (SettingsGeneralModel) reader.readFrom(cast(SettingsGeneralModel.class),
                SettingsGeneralModel.class, null, null, null,
                stream("title: Example\nbaseUrl: https://example.com\n"));

        assertEquals("Example", model.getTitle());
        assertEquals("https://example.com", model.getBaseUrl().toString());
    }

    @Test
    void testReadsNestedModelFromYaml() throws Exception {
        final MailServerModel model = (MailServerModel) reader.readFrom(cast(MailServerModel.class),
                MailServerModel.class, null, null, null,
                stream("smtp:\n  host: localhost\n  port: 25\n"));

        assertEquals("localhost", model.getSmtp().getHost());
        assertEquals(25, model.getSmtp().getPort());
    }

    @Test
    void testReadsPolymorphicModelFromYaml() throws Exception {
        final Method method = TypeCarrier.class.getDeclaredMethod("directories");
        final Type mapType = method.getGenericReturnType();

        @SuppressWarnings("unchecked")
        final Map<String, AbstractDirectoryModel> directories = (Map<String, AbstractDirectoryModel>)
                reader.readFrom(cast(Map.class), mapType, null, null, null,
                        stream("internal:\n  type: internal\n  description: An internal directory\n"));

        assertInstanceOf(DirectoryInternalModel.class, directories.get("internal"));
        assertEquals("An internal directory", directories.get("internal").getDescription());
    }

    @Test
    void testMalformedYamlIsBadRequest() {
        assertThrows(BadRequestException.class, () -> reader.readFrom(cast(SettingsGeneralModel.class),
                SettingsGeneralModel.class, null, null, null,
                stream("title: [unclosed\n")));
    }

    @SuppressWarnings("unused")
    private interface TypeCarrier {
        Map<String, AbstractDirectoryModel> directories();
    }

    @SuppressWarnings("unchecked")
    private static Class<Object> cast(final Class<?> type) {
        return (Class<Object>) type;
    }

    private static InputStream stream(final String yaml) {
        return new ByteArrayInputStream(yaml.getBytes(StandardCharsets.UTF_8));
    }
}
