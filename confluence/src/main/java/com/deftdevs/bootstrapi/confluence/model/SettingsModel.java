package com.deftdevs.bootstrapi.confluence.model;

import com.deftdevs.bootstrapi.commons.model.AbstractSettingsModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.SETTINGS;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = SETTINGS)
public class SettingsModel extends AbstractSettingsModel {

    @XmlElement
    private SettingsBrandingModel branding;

    @XmlElement
    private SettingsCustomHtmlModel customHtml;

}
