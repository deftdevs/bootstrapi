package de.aservo.confapi.commons.model;

import de.aservo.confapi.commons.constants.ConfAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.Collections;

/**
 * Bean for directories in REST requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = ConfAPI.APPLICATION_LINKS)
public class ApplicationLinksBean {

    @XmlElement
    private Collection<ApplicationLinkBean> applicationLinks;

    // Example instances for documentation and tests

    public static final ApplicationLinksBean EXAMPLE_1 = new ApplicationLinksBean(Collections.singleton(ApplicationLinkBean.EXAMPLE_1));

}
