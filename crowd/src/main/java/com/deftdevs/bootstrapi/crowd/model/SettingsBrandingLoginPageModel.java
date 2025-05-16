package com.deftdevs.bootstrapi.crowd.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlRootElement(name = BootstrAPI.SETTINGS_BRANDING_LOGIN_PAGE)
public class SettingsBrandingLoginPageModel {

    @XmlElement
    private Boolean showLogo;

    @XmlElement
    private String header;

    @XmlElement
    private String content;

    @XmlElement
    private String buttonColor;

    public static final SettingsBrandingLoginPageModel EXAMPLE_1;
    public static final SettingsBrandingLoginPageModel EXAMPLE_2;

    static {
        EXAMPLE_1 = new SettingsBrandingLoginPageModel();
        EXAMPLE_1.setHeader("Default header");
        EXAMPLE_1.setContent("Default content");
        EXAMPLE_1.setShowLogo(false);
        EXAMPLE_1.setButtonColor("#ffffff");
    }

    static {
        EXAMPLE_2 = new SettingsBrandingLoginPageModel();
        EXAMPLE_2.setHeader("Example header");
        EXAMPLE_2.setContent("Example content");
        EXAMPLE_2.setShowLogo(true);
        EXAMPLE_2.setButtonColor("#000000");
    }
}
