package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

/**
 * Bean for a gadget in REST requests.
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = BootstrAPI.GADGET)
public class GadgetBean {

    @XmlElement
    private Long id;

    @XmlElement
    private URI url;

    // Example instances for documentation and tests

    public static final GadgetBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new GadgetBean();
        EXAMPLE_1.setUrl(URI.create("http://localhost/gadget"));
    }

}
