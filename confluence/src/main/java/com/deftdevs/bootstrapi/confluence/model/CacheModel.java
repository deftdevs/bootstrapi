package com.deftdevs.bootstrapi.confluence.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlRootElement(name = BootstrAPI.CACHE)
public class CacheModel {

    @NotNull
    @XmlElement
    private String name;

    @XmlElement
    private Integer maxObjectCount;

    @XmlElement
    private Long currentHeapSizeInByte;

    @XmlElement
    private Double effectivenessInPercent;

    @XmlElement
    private Double utilisationInPercent;

    @XmlElement
    private Boolean flushable;

}
