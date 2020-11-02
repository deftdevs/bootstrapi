package de.aservo.confapi.commons.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.aservo.confapi.commons.constants.ConfAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        property = "type"
)
@org.codehaus.jackson.annotate.JsonTypeInfo(
        use = org.codehaus.jackson.annotate.JsonTypeInfo.Id.NAME,
        include = org.codehaus.jackson.annotate.JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @Type(value = DirectoryCrowdBean.class, name = ConfAPI.DIRECTORY_CROWD),
        @Type(value = DirectoryGenericBean.class, name = ConfAPI.DIRECTORY_GENERIC),
        @Type(value = DirectoryInternalBean.class, name = ConfAPI.DIRECTORY_INTERNAL),
        @Type(value = DirectoryLdapBean.class, name = ConfAPI.DIRECTORY_LDAP),
})
@org.codehaus.jackson.annotate.JsonSubTypes({
        @org.codehaus.jackson.annotate.JsonSubTypes.Type(value = DirectoryCrowdBean.class, name = ConfAPI.DIRECTORY_CROWD),
        @org.codehaus.jackson.annotate.JsonSubTypes.Type(value = DirectoryGenericBean.class, name = ConfAPI.DIRECTORY_GENERIC),
        @org.codehaus.jackson.annotate.JsonSubTypes.Type(value = DirectoryInternalBean.class, name = ConfAPI.DIRECTORY_INTERNAL),
        @org.codehaus.jackson.annotate.JsonSubTypes.Type(value = DirectoryLdapBean.class, name = ConfAPI.DIRECTORY_LDAP),
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
