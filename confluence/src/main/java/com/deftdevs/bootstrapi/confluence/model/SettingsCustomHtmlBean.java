package com.deftdevs.bootstrapi.confluence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = SETTINGS + "-" + SETTINGS_CUSTOM_HTML)
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsCustomHtmlBean {

    @XmlElement
    private String beforeHeadEnd;

    @XmlElement
    private String afterBodyStart;

    @XmlElement
    private String beforeBodyEnd;

    // Example instances for documentation and tests

    public static final SettingsCustomHtmlBean EXAMPLE_1 = SettingsCustomHtmlBean.builder()
            .beforeHeadEnd("Before head end")
            .afterBodyStart("After body start")
            .beforeBodyEnd("Before body end")
            .build();

}
