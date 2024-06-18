package de.aservo.confapi.crowd.model;

import de.aservo.confapi.crowd.rest.api.TrustedProxiesResource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = TrustedProxiesResource.TRUSTED_PROXIES)
public class TrustedProxiesBean {

    @XmlElement
    private Collection<String> trustedProxies;

    public static final TrustedProxiesBean EXAMPLE_1;
    public static final TrustedProxiesBean EXAMPLE_2;

    static {
        EXAMPLE_1 = new TrustedProxiesBean();
        EXAMPLE_1.setTrustedProxies(Collections.singletonList("0.0.0.0/0"));
    }

    static {
        EXAMPLE_2 = new TrustedProxiesBean();
        EXAMPLE_2.setTrustedProxies(Collections.singletonList("10.10.10.10/10")); // NOSONAR - hardcoded IP only for testing
    }
}
