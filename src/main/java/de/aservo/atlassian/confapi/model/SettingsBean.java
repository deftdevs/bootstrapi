package de.aservo.atlassian.confapi.model;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
        if (StringUtils.isNotBlank(mode)) {
            return mode.toLowerCase();
        }

        return null;
    }

    // Example instances for documentation and tests

    public static final SettingsBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new SettingsBean();
        EXAMPLE_1.setTitle("Example");
        EXAMPLE_1.setBaseUrl("https://example.com");
        EXAMPLE_1.setMode("private");
    }

}
