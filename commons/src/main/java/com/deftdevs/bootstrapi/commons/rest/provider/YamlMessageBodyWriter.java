package com.deftdevs.bootstrapi.commons.rest.provider;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Serializes response models as YAML when the client asks for it
 * ({@code Accept: application/yaml}); the default response format
 * remains JSON.
 */
@Provider
@Produces({BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
public class YamlMessageBodyWriter implements MessageBodyWriter<Object> {

    @Override
    public boolean isWriteable(
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType) {

        return true;
    }

    @Override
    public long getSize(
            final Object object,
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType) {

        return -1;
    }

    @Override
    public void writeTo(
            final Object object,
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType,
            final MultivaluedMap<String, Object> httpHeaders,
            final OutputStream entityStream) throws IOException {

        YamlObjectMapperHolder.YAML_OBJECT_MAPPER.writeValue(entityStream, object);
    }
}
