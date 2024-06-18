package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
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
@XmlRootElement(name = BootstrAPI.APPLICATION_LINKS)
public class ApplicationLinksBean {

    @XmlElement
    private Collection<ApplicationLinkBean> applicationLinks;

    // Example instances for documentation and tests

    public static final ApplicationLinksBean EXAMPLE_1 = new ApplicationLinksBean(Collections.singleton(ApplicationLinkBean.EXAMPLE_1));

}
