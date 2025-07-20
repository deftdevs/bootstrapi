package com.deftdevs.bootstrapi.crowd.model;

import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import lombok.Data;
import lombok.Builder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@Builder
@XmlRootElement(name = "all")
public class AllModel {

    @XmlElement
    private SettingsModel settings;

    @XmlElement
    private List<ApplicationModel> applications;

}
