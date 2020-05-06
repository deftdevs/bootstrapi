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
public class ApplicationLinksBean {

    @XmlElement
    private Collection<ApplicationLinkBean> applicationLinks;

    // Example instances for documentation and tests

    public static final ApplicationLinksBean EXAMPLE_1 = new ApplicationLinksBean(Collections.singleton(ApplicationLinkBean.EXAMPLE_1));

}
