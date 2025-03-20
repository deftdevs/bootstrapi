package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Date;

/**
 * Bean for a licence info in REST requests.
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = BootstrAPI.LICENSE)
public class LicenseBean {

    @XmlElement
    private List<String> products;

    @XmlElement
    private String type;

    @XmlElement
    private String organization;

    @XmlElement
    private String description;

    @XmlElement
    private Date expiryDate;

    @XmlElement
    private int maxUsers;

    // Example instances for documentation and tests

    public static final LicenseBean EXAMPLE_1;
    public static final LicenseBean EXAMPLE_2_DEVELOPER_LICENSE;

    static {
        EXAMPLE_1 = new LicenseBean();
        EXAMPLE_1.setDescription("Example License");
        EXAMPLE_1.setOrganization("Example Organization");
        EXAMPLE_1.setProducts(Collections.singletonList("example-product"));
        EXAMPLE_1.setMaxUsers(10);
        EXAMPLE_1.setExpiryDate(new Date());

        // use "3 hour expiration for all Atlassian host products*"
        // from https://developer.atlassian.com/platform/marketplace/timebomb-licenses-for-testing-server-apps/
        EXAMPLE_2_DEVELOPER_LICENSE = new LicenseBean();
        EXAMPLE_2_DEVELOPER_LICENSE.setDescription("Test license for plugin developers");
        EXAMPLE_2_DEVELOPER_LICENSE.setType("TESTING");
        EXAMPLE_2_DEVELOPER_LICENSE.setOrganization("Atlassian");
        EXAMPLE_2_DEVELOPER_LICENSE.setProducts(Arrays.asList(
                "Confluence",
                "Jira",
                "Bitbucket",
                "Crowd",
                "Bamboo",
                "Fisheye"));
        EXAMPLE_2_DEVELOPER_LICENSE.setMaxUsers(1);
        EXAMPLE_2_DEVELOPER_LICENSE.setExpiryDate(new Date());
    }

}
