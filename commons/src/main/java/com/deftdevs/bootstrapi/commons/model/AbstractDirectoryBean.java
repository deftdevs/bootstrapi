package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Bean for user directory settings in REST requests.
 */
@Data
@NoArgsConstructor
@XmlRootElement
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
// Note: New subtypes must also be registered in DirectoriesBean.java to be considered in the REST API documentation
@JsonSubTypes({
        @JsonSubTypes.Type(value = DirectoryCrowdBean.class, name = BootstrAPI.DIRECTORY_CROWD),
        @JsonSubTypes.Type(value = DirectoryDelegatingBean.class, name = BootstrAPI.DIRECTORY_DELEGATING),
        @JsonSubTypes.Type(value = DirectoryGenericBean.class, name = BootstrAPI.DIRECTORY_GENERIC),
        @JsonSubTypes.Type(value = DirectoryInternalBean.class, name = BootstrAPI.DIRECTORY_INTERNAL),
        @JsonSubTypes.Type(value = DirectoryLdapBean.class, name = BootstrAPI.DIRECTORY_LDAP),
})
public abstract class AbstractDirectoryBean {

    @XmlElement
    private Long id;

    @XmlElement
    @NotNull
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
