package com.deftdevs.bootstrapi.crowd.model;

import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = ConfAPI.APPLICATIONS)
public class ApplicationsBean {

    @XmlElement
    private Collection<ApplicationBean> applications;

}
