package com.deftdevs.bootstrapi.confluence.model;

import com.deftdevs.bootstrapi.commons.model.SettingsBrandingColorSchemeModel;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.SETTINGS;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = SETTINGS)
public class SettingsModel {

    @XmlElement
    private SettingsGeneralModel general;

    @XmlElement
    private SettingsSecurityModel security;

    @XmlElement
    private SettingsBrandingColorSchemeModel branding;

    @XmlElement
    private SettingsCustomHtmlModel customHtml;

}
