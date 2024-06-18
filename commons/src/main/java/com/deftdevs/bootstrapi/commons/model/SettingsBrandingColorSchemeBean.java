package com.deftdevs.bootstrapi.commons.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.deftdevs.bootstrapi.commons.constants.ConfAPI.*;

@Data
@NoArgsConstructor
@XmlRootElement(name = SETTINGS + "-" + BRANDING + "-" + COLOR_SCHEME)
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsBrandingColorSchemeBean {

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

    @XmlElement
    private String pageMenuSelectedBackground;

    @XmlElement
    private String pageMenuItemText;

    @XmlElement
    private String headingText;

    @XmlElement
    private String links;

    @XmlElement
    private String bordersAndDividers;

    // Example instances for documentation and tests

    public static final SettingsBrandingColorSchemeBean EXAMPLE_1;

    static {
        final String COLOR_WHITE = "#FFFFFF";

        EXAMPLE_1 = new SettingsBrandingColorSchemeBean();
        EXAMPLE_1.setBordersAndDividers(COLOR_WHITE);
        EXAMPLE_1.setHeaderButtonBackground(COLOR_WHITE);
        EXAMPLE_1.setHeaderButtonText(COLOR_WHITE);
        EXAMPLE_1.setHeadingText(COLOR_WHITE);
        EXAMPLE_1.setLinks(COLOR_WHITE);
        EXAMPLE_1.setMenuItemSelectedBackground(COLOR_WHITE);
        EXAMPLE_1.setMenuItemSelectedText(COLOR_WHITE);
        EXAMPLE_1.setPageMenuItemText(COLOR_WHITE);
        EXAMPLE_1.setPageMenuSelectedBackground(COLOR_WHITE);
        EXAMPLE_1.setSearchFieldBackground(COLOR_WHITE);
        EXAMPLE_1.setSearchFieldText(COLOR_WHITE);
        EXAMPLE_1.setTopBar(COLOR_WHITE);
        EXAMPLE_1.setTopBarMenuSelectedBackground(COLOR_WHITE);
        EXAMPLE_1.setTopBarMenuItemText(COLOR_WHITE);
    }
}
