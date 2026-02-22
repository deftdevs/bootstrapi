package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Model for user directory settings in REST requests.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DirectoryCrowdModel.class, name = BootstrAPI.DIRECTORY_CROWD),
        @JsonSubTypes.Type(value = DirectoryDelegatingModel.class, name = BootstrAPI.DIRECTORY_DELEGATING),
        @JsonSubTypes.Type(value = DirectoryGenericModel.class, name = BootstrAPI.DIRECTORY_GENERIC),
        @JsonSubTypes.Type(value = DirectoryInternalModel.class, name = BootstrAPI.DIRECTORY_INTERNAL),
        @JsonSubTypes.Type(value = DirectoryLdapModel.class, name = BootstrAPI.DIRECTORY_LDAP),
})
// Note: The subtypes must also be registered for swagger to be considered in the REST API documentation
@Schema(anyOf = {
        DirectoryCrowdModel.class,
        DirectoryDelegatingModel.class,
        DirectoryGenericModel.class,
        DirectoryInternalModel.class,
        DirectoryLdapModel.class,
})
public abstract class AbstractDirectoryModel {

    @XmlElement
    private Long id;

    @XmlElement
    private String name;

    @XmlElement
    private String description;

    @XmlElement
    private Boolean active;

    @XmlElement
    private Date createdDate;

    @XmlElement
    private Date updatedDate;

}
