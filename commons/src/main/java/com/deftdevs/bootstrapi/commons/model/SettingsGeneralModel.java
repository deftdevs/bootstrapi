package com.deftdevs.bootstrapi.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import com.deftdevs.bootstrapi.commons.model.type.SubEntityOf;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.SETTINGS_GENERAL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SubEntityOf(AbstractSettingsModel.class)
@XmlRootElement(name = SETTINGS_GENERAL)
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsGeneralModel {

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

    public static final SettingsGeneralModel EXAMPLE_1 = SettingsGeneralModel.builder()
        .title("Example")
        .baseUrl(URI.create("https://example.com"))
        .mode("private")
        .contactMessage("Test Message")
        .externalUserManagement(true)
        .build();

    public static final SettingsGeneralModel EXAMPLE_1_NO_MODE = SettingsGeneralModel.builder()
        .title("Example")
        .baseUrl(URI.create("https://example.com"))
        .mode(null)
        .contactMessage("Test Message")
        .externalUserManagement(true)
        .build();

    // restricted to the fields supported by all products (e.g. Crowd)
    public static final SettingsGeneralModel EXAMPLE_1_MINIMAL = SettingsGeneralModel.builder()
        .title(EXAMPLE_1.getTitle())
        .baseUrl(EXAMPLE_1.getBaseUrl())
        .build();

}
