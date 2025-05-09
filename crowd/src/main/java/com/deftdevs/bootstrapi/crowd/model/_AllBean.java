package com.deftdevs.bootstrapi.crowd.model;

import com.deftdevs.bootstrapi.commons.model.GroupBean;
import com.deftdevs.bootstrapi.commons.model.SettingsBean;
import com.deftdevs.bootstrapi.commons.model.UserBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "all")
public class _AllBean {

    @XmlElement
    private SettingsBean settings;

    @XmlElement
    private Map<String, UserBean> users;

    @XmlElement
    private Map<String, GroupBean> groups;

    @XmlElement
    private Map<String, ApplicationBean> applications;

    @XmlElement
    private Map<String, _AllBeanConfigStatus> status;
}
