package com.deftdevs.bootstrapi.commons.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlElement;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractDirectoryExternalModel extends AbstractDirectoryModel {

    @XmlElement
    private Boolean testConnection;

}
