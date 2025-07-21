package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Date;

/**
 * Model for a licence info in REST requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = BootstrAPI.LICENSE)
public class LicenseModel {

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

    public static final LicenseModel EXAMPLE_1 = LicenseModel.builder()
            .description("Example License")
            .organization("Example Organization")
            .products(Collections.singletonList("example-product"))
            .maxUsers(10)
            .expiryDate(new Date())
            .build();

    // use "3 hour expiration for all Atlassian host products*"
    // from https://developer.atlassian.com/platform/marketplace/timebomb-licenses-for-testing-server-apps/
    public static final LicenseModel EXAMPLE_2_DEVELOPER_LICENSE = LicenseModel.builder()
            .description("Test license for plugin developers")
            .type("TESTING")
            .organization("Atlassian")
            .products(Arrays.asList(
                    "Confluence",
                    "Jira",
                    "Bitbucket",
                    "Crowd",
                    "Bamboo",
                    "Fisheye"))
            .maxUsers(1)
            .expiryDate(new Date())
            .build();

}
