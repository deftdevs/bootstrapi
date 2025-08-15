package com.deftdevs.bootstrapi.crowd.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public static final SettingsBrandingLoginPageModel EXAMPLE_1 = SettingsBrandingLoginPageModel.builder()
        .header("Default header")
        .content("Default content")
        .showLogo(false)
        .buttonColor("#ffffff")
        .build();

    public static final SettingsBrandingLoginPageModel EXAMPLE_2 = SettingsBrandingLoginPageModel.builder()
        .header("Example header")
        .content("Example content")
        .showLogo(true)
        .buttonColor("#000000")
        .build();

}
