package de.aservo.atlassian.confapi.model;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.Collections;

/**
 * Bean for directories in REST requests.
 */
@Data
@AllArgsConstructor
@XmlRootElement(name = ConfAPI.DIRECTORIES)
public class DirectoriesBean {

    @XmlElement
    private Collection<DirectoryBean> directories;

    // Example instances for documentation and tests

    public static final DirectoriesBean EXAMPLE_1 = new DirectoriesBean(Collections.singleton(DirectoryBean.EXAMPLE_1));

}
