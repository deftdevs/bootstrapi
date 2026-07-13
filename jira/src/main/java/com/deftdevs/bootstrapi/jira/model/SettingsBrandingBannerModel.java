package com.deftdevs.bootstrapi.jira.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import com.deftdevs.bootstrapi.commons.model.type.SubEntityOf;
import javax.xml.bind.annotation.XmlRootElement;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.SETTINGS_BRANDING_BANNER;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SubEntityOf(SettingsBrandingModel.class)
@XmlRootElement(name = SETTINGS_BRANDING_BANNER)
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsBrandingBannerModel {

    @XmlElement
    private String content;

    @XmlElement
    private Visibility visibility;

    public enum Visibility {
        PUBLIC,
        PRIVATE,
    }

    // Example instances for documentation and tests

    public static final SettingsBrandingBannerModel EXAMPLE_1 = SettingsBrandingBannerModel.builder()
            .content("")
            .visibility(Visibility.PUBLIC)
            .build();

}
