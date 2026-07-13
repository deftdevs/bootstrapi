package com.deftdevs.bootstrapi.commons.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlElement;

/**
 * Canonical settings group schema shared by all products. Products extend
 * it with the members they additionally support (a branding group, custom
 * HTML, the banner), so shared field names match by construction. Crowd
 * cannot apply security settings and rejects them with a per-field error
 * status.
 */
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractSettingsModel {

    @XmlElement
    private SettingsGeneralModel general;

    @XmlElement
    private SettingsSecurityModel security;

}
