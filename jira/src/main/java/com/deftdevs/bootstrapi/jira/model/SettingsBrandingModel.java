package com.deftdevs.bootstrapi.jira.model;

import com.deftdevs.bootstrapi.commons.model.type.SubEntityOf;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.SETTINGS_BRANDING;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SubEntityOf(SettingsModel.class)
@XmlRootElement(name = SETTINGS_BRANDING)
public class SettingsBrandingModel {

    @XmlElement
    private SettingsBrandingBannerModel banner;

}
