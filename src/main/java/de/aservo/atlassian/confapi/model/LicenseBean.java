package de.aservo.atlassian.confapi.model;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * Bean for a licence info in REST requests.
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = ConfAPI.LICENSE)
public class LicenseBean {

    @XmlElement
    private String licenseType;

    @XmlElement
    private String organization;

    @XmlElement
    private String description;

    @XmlElement
    private Date expiryDate;

    @XmlElement
    private int numUsers;

    @XmlElement
    private String key;

    @XmlElement
    private Collection<String> products;

    // Example instances for documentation and tests

    public static final LicenseBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new LicenseBean();
        EXAMPLE_1.setKey("ABC...");
        EXAMPLE_1.setDescription("Example License");
        EXAMPLE_1.setOrganization("Example Organization");
        EXAMPLE_1.setProducts(Collections.singleton("example-product"));
        EXAMPLE_1.setNumUsers(10);
        EXAMPLE_1.setExpiryDate(new Date());
    }

}
