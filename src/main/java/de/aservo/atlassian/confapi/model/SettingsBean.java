package de.aservo.atlassian.confapi.model;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@Data
@NoArgsConstructor
@XmlRootElement(name = ConfAPI.SETTINGS)
public class SettingsBean {

    @XmlElement
    private String baseUrl;

    @XmlElement
    private String mode;

    @XmlElement
    private String title;

    public String getMode() {
        if (isNotBlank(mode)) {
            return mode.toLowerCase();
        }

        return null;
    }

}
