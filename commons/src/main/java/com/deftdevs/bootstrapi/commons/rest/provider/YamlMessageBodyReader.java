package com.deftdevs.bootstrapi.commons.rest.provider;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Deserializes YAML request bodies into the same models as the JSON
 * provider, so any endpoint accepting a configuration also accepts it as
 * YAML ({@code Content-Type: application/yaml}).
 */
@Provider
@Consumes({BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
public class YamlMessageBodyReader implements MessageBodyReader<Object> {

    @Override
    public boolean isReadable(
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType) {

        return true;
    }

    @Override
    public Object readFrom(
            final Class<Object> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType,
            final MultivaluedMap<String, String> httpHeaders,
            final InputStream entityStream) throws IOException {

        try {
            return YamlObjectMapperHolder.YAML_OBJECT_MAPPER.readValue(entityStream,
                    YamlObjectMapperHolder.YAML_OBJECT_MAPPER.getTypeFactory()
                            .constructType(genericType != null ? genericType : type));
        } catch (JsonProcessingException e) {
            throw new BadRequestException(e);
        }
    }
}
