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
 * Bean for a gadget in REST requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = ConfAPI.GADGETS)
public class GadgetsBean {

    @XmlElement
    private Collection<GadgetBean> gadgets;

    // Example instances for documentation and tests

    public static final GadgetsBean EXAMPLE_1 = new GadgetsBean(Collections.singleton(GadgetBean.EXAMPLE_1));

}
