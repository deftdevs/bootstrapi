package com.deftdevs.bootstrapi.jira.model;

import lombok.Data;
import lombok.Builder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.SETTINGS;
import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.SETTINGS_BANNER;

@Data
@Builder
@XmlRootElement(name = SETTINGS + "-" + SETTINGS_BANNER)
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsBannerModel {

    @XmlElement
    private String content;

    @XmlElement
    private Visibility visibility;

    public enum Visibility {
        PUBLIC,
        PRIVATE,
    }

    // Example instances for documentation and tests

    public static final SettingsBannerModel EXAMPLE_1 = SettingsBannerModel.builder()
            .content("")
            .visibility(Visibility.PUBLIC)
            .build();

}
