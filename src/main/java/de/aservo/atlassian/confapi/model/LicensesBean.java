package de.aservo.atlassian.confapi.model;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

/**
 * Bean for licences infos in REST requests.
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = ConfAPI.LICENSES)
public class LicensesBean {

    @XmlElement
    private Collection<LicenseBean> licenses;
}