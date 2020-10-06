package de.aservo.confapi.commons.model;

import de.aservo.confapi.commons.constants.ConfAPI;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

/**
 * Bean for directories in REST requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = ConfAPI.DIRECTORIES)
public class DirectoriesBean {

    @XmlElement
    @Schema(anyOf = {
            DirectoryCrowdBean.class,
            DirectoryGenericBean.class,
            DirectoryInternalBean.class,
            DirectoryLdapBean.class,
    })
    private Collection<AbstractDirectoryBean> directories;

}
