package de.aservo.confapi.commons.model;

import de.aservo.confapi.commons.constants.ConfAPI;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Data
@NoArgsConstructor
@XmlRootElement(name = ConfAPI.SETTINGS)
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsBean implements Serializable {

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
    public static final SettingsBean EXAMPLE_1_NO_MODE;

    static {
        EXAMPLE_1 = new SettingsBean();
        EXAMPLE_1.setTitle("Example");
        EXAMPLE_1.setBaseUrl("https://example.com");
        EXAMPLE_1.setMode("private");

        EXAMPLE_1_NO_MODE = new SettingsBean();
        EXAMPLE_1_NO_MODE.setTitle("Example");
        EXAMPLE_1_NO_MODE.setBaseUrl("https://example.com");
        EXAMPLE_1_NO_MODE.setMode(null);
    }

}
