package com.deftdevs.bootstrapi.commons.model.type;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = BootstrAPI.PERMISSIONS)
public class DirectoryPermissions {

    @XmlElement
    private Boolean addGroup;

    @XmlElement
    private Boolean addUser;

    @XmlElement
    private Boolean modifyGroup;

    @XmlElement
    private Boolean modifyUser;

    @XmlElement
    private Boolean modifyGroupAttributes;

    @XmlElement
    private Boolean modifyUserAttributes;

    @XmlElement
    private Boolean removeGroup;

    @XmlElement
    private Boolean removeUser;

}
