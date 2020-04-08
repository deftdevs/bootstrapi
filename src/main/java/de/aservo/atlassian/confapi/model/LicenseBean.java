package de.aservo.atlassian.confapi.model;

import com.atlassian.sal.api.license.SingleProductLicenseDetailsView;
import de.aservo.atlassian.confapi.constants.ConfAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * Bean for licence infos in REST requests.
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

    /**
     * Instantiates a new License bean.
     *
     * @param productLicense the product license
     */
    public LicenseBean(SingleProductLicenseDetailsView productLicense) {
        products = Collections.singletonList(productLicense.getProductDisplayName());
        licenseType = productLicense.getLicenseTypeName();
        organization = productLicense.getOrganisationName();
        description = productLicense.getDescription();
        expiryDate = productLicense.getMaintenanceExpiryDate();
        numUsers = productLicense.getNumberOfUsers();
    }
}