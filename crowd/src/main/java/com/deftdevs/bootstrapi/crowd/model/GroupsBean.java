package com.deftdevs.bootstrapi.crowd.model;

import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import com.deftdevs.bootstrapi.commons.model.GroupBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = ConfAPI.GROUPS)
public class GroupsBean {

    @XmlElement
    private Collection<GroupBean> groups;

}