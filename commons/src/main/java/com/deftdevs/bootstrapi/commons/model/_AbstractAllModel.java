package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.model.type._AllModelAccessor;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.xml.bind.annotation.XmlElement;
import java.util.Map;

/**
 * Shared shape of the product _all models: the configuration fields every
 * product supports plus the per-sub-field status map of the response.
 * Products extend it with their product-specific fields.
 *
 * @param <S> the product's settings model type
 */
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class _AbstractAllModel<S> implements _AllModelAccessor {

    @XmlElement
    private S settings;

    @XmlElement
    private Map<String, AbstractDirectoryModel> directories;

    @XmlElement
    private Map<String, ApplicationLinkModel> applicationLinks;

    @XmlElement
    private Map<String, LicenseModel> licenses;

    @XmlElement
    private MailServerModel mailServer;

    @XmlElement
    private UpmModel upm;

    @XmlElement
    private Map<String, _AllModelStatus> status;

}
