package de.aservo.confapi.crowd.model;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.GroupBean;
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
