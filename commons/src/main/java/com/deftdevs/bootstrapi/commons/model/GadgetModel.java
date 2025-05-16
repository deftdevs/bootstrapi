package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

/**
 * Model for a gadget in REST requests.
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = BootstrAPI.GADGET)
public class GadgetModel {

    @XmlElement
    private Long id;

    @XmlElement
    private URI url;

    // Example instances for documentation and tests

    public static final GadgetModel EXAMPLE_1;

    static {
        EXAMPLE_1 = new GadgetModel();
        EXAMPLE_1.setUrl(URI.create("http://localhost/gadget"));
    }

}
