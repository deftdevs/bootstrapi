package com.deftdevs.bootstrapi.confluence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import com.deftdevs.bootstrapi.commons.model.type.SubEntityOf;
import javax.xml.bind.annotation.XmlRootElement;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SubEntityOf(SettingsBrandingModel.class)
@XmlRootElement(name = SETTINGS_BRANDING_CUSTOM_HTML)
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsBrandingCustomHtmlModel {

    @XmlElement
    private String beforeHeadEnd;

    @XmlElement
    private String afterBodyStart;

    @XmlElement
    private String beforeBodyEnd;

    // Example instances for documentation and tests

    public static final SettingsBrandingCustomHtmlModel EXAMPLE_1 = SettingsBrandingCustomHtmlModel.builder()
            .beforeHeadEnd("Before head end")
            .afterBodyStart("After body start")
            .beforeBodyEnd("Before body end")
            .build();

}
