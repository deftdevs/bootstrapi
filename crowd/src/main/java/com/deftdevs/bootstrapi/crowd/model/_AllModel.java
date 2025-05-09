package com.deftdevs.bootstrapi.crowd.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = BootstrAPI._ALL)
public class _AllModel {

    @XmlElement
    private SettingsModel settings;

    @XmlElement
    private Map<String, ApplicationModel> applications;

    @XmlElement
    private Map<String, AbstractDirectoryModel> directories;

    @XmlElement
    private Map<String, _AllModelStatus> status;

}
