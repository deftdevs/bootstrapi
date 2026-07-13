package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.type.SubEntityOf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SubEntityOf(PermissionsModel.class)
@XmlRootElement(name = BootstrAPI.PERMISSIONS_GLOBAL)
public class PermissionsGlobalModel {

    @XmlElement
    private Map<String, ? extends Collection<String>> groups;

    @XmlElement
    private List<String> anonymous;

}
