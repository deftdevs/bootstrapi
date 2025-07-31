package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = BootstrAPI.SETTINGS)
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsModel {

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

    public static final SettingsModel EXAMPLE_1 = SettingsModel.builder()
        .title("Example")
        .baseUrl(URI.create("https://example.com"))
        .mode("private")
        .contactMessage("Test Message")
        .externalUserManagement(true)
        .build();

    public static final SettingsModel EXAMPLE_1_NO_MODE = SettingsModel.builder()
        .title("Example")
        .baseUrl(URI.create("https://example.com"))
        .mode(null)
        .contactMessage("Test Message")
        .externalUserManagement(true)
        .build();

}
