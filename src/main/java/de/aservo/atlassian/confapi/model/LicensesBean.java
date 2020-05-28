package de.aservo.atlassian.confapi.model;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.Collections;

/**
 * Bean for licences info in REST requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = ConfAPI.LICENSES)
public class LicensesBean {

    @XmlElement
    private Collection<LicenseBean> licenses;

    // Example instances for documentation and tests

    public static final LicensesBean EXAMPLE_1 = new LicensesBean(Collections.singleton(LicenseBean.EXAMPLE_1));
    public static final LicensesBean EXAMPLE_2_DEVELOPER_LICENSE = new LicensesBean(Collections.singleton(LicenseBean.EXAMPLE_2_DEVELOPER_LICENSE));

}
