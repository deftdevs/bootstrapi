package de.aservo.atlassian.confapi.model;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean for a gadget in REST requests.
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = ConfAPI.GADGET)
public class GadgetBean {

    @XmlElement
    private String url;

    // Example instances for documentation and tests

    public static final GadgetBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new GadgetBean();
        EXAMPLE_1.setUrl("http://localhost/gadget");
    }

}
