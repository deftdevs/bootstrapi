package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
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
@XmlRootElement(name = BootstrAPI.DIRECTORIES)
public class DirectoriesBean {

    @XmlElement
    @Schema(anyOf = {
            DirectoryCrowdBean.class,
            DirectoryDelegatingBean.class,
            DirectoryGenericBean.class,
            DirectoryInternalBean.class,
            DirectoryLdapBean.class,
    })
    private Collection<AbstractDirectoryBean> directories;

}
