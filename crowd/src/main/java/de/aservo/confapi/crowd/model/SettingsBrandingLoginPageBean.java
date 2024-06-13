package de.aservo.confapi.crowd.model;

import de.aservo.confapi.commons.constants.ConfAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlRootElement(name = ConfAPI.SETTINGS_BRANDING_LOGIN_PAGE)
public class SettingsBrandingLoginPageBean {

    @XmlElement
    private Boolean showLogo;

    @XmlElement
    private String header;

    @XmlElement
    private String content;

    @XmlElement
    private String buttonColor;

    public static final SettingsBrandingLoginPageBean EXAMPLE_1;
    public static final SettingsBrandingLoginPageBean EXAMPLE_2;

    static {
        EXAMPLE_1 = new SettingsBrandingLoginPageBean();
        EXAMPLE_1.setHeader("Default header");
        EXAMPLE_1.setContent("Default content");
        EXAMPLE_1.setShowLogo(false);
        EXAMPLE_1.setButtonColor("#ffffff");
    }

    static {
        EXAMPLE_2 = new SettingsBrandingLoginPageBean();
        EXAMPLE_2.setHeader("Example header");
        EXAMPLE_2.setContent("Example content");
        EXAMPLE_2.setShowLogo(true);
        EXAMPLE_2.setButtonColor("#000000");
    }
}
