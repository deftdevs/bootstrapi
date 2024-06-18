package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@Data
@NoArgsConstructor
@XmlRootElement(name = BootstrAPI.SETTINGS)
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsBean {

    @XmlElement
    private URI baseUrl;

    @XmlElement
    private String mode;

    @XmlElement
    private String title;

    @XmlElement
    private String contactMessage;

    @XmlElement
    private Boolean externalUserManagement;

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
        EXAMPLE_1.setBaseUrl(URI.create("https://example.com"));
        EXAMPLE_1.setMode("private");
        EXAMPLE_1.setContactMessage("Test Message");
        EXAMPLE_1.setExternalUserManagement(true);

        EXAMPLE_1_NO_MODE = new SettingsBean();
        EXAMPLE_1_NO_MODE.setTitle("Example");
        EXAMPLE_1_NO_MODE.setBaseUrl(URI.create("https://example.com"));
        EXAMPLE_1_NO_MODE.setMode(null);
        EXAMPLE_1_NO_MODE.setContactMessage("Test Message");
        EXAMPLE_1_NO_MODE.setExternalUserManagement(true);
    }

}
