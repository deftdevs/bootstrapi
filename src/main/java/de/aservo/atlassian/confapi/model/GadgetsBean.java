package de.aservo.atlassian.confapi.model;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

/**
 * Bean for a gadget in REST requests.
 */
@Data
@AllArgsConstructor
@XmlRootElement(name = ConfAPI.GADGETS)
public class GadgetsBean {

    @XmlElement
    private Collection<GadgetBean> gadgets;

}
