package com.deftdevs.bootstrapi.crowd.model;

import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
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
    private SettingsBrandingLoginPageModel branding;

}
