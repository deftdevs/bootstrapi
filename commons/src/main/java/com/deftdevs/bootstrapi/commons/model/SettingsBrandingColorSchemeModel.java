package com.deftdevs.bootstrapi.commons.model;

import lombok.Data;
import lombok.Builder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.*;

@Data
@Builder
@XmlRootElement(name = SETTINGS + "-" + SETTINGS_BRANDING + "-" + COLOR_SCHEME)
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsBrandingColorSchemeModel {

    @XmlElement
    private String topBar;

    @XmlElement
    private String topBarText;

    @XmlElement
    private String headerButtonBackground;

    @XmlElement
    private String headerButtonText;

    @XmlElement
    private String topBarMenuSelectedBackground;

    @XmlElement
    private String topBarMenuSelectedText;

    @XmlElement
    private String topBarMenuItemText;

    @XmlElement
    private String menuItemSelectedBackground;

    @XmlElement
    private String menuItemSelectedText;

    @XmlElement
    private String searchFieldBackground;

    @XmlElement
    private String searchFieldText;

    // @XmlElement
    // private String pageMenuSelectedBackground; // REMOVED: field not supported by util, builder, or ColourScheme

    @XmlElement
    private String pageMenuItemText;

    @XmlElement
    private String headingText;

    @XmlElement
    private String links;

    @XmlElement
    private String bordersAndDividers;

    // Example instances for documentation and tests

    private static final String COLOR_WHITE = "#FFFFFF";

    public static final SettingsBrandingColorSchemeModel EXAMPLE_1 = SettingsBrandingColorSchemeModel.builder()
        .bordersAndDividers(COLOR_WHITE)
        .headerButtonBackground(COLOR_WHITE)
        .headerButtonText(COLOR_WHITE)
        .headingText(COLOR_WHITE)
        .links(COLOR_WHITE)
        .menuItemSelectedBackground(COLOR_WHITE)
        .menuItemSelectedText(COLOR_WHITE)
        .pageMenuItemText(COLOR_WHITE)
        // .pageMenuSelectedBackground(COLOR_WHITE) // REMOVED: field does not exist
        .searchFieldBackground(COLOR_WHITE)
        .searchFieldText(COLOR_WHITE)
        .topBar(COLOR_WHITE)
        .topBarMenuSelectedBackground(COLOR_WHITE)
        .topBarMenuItemText(COLOR_WHITE)
        .build();

}
