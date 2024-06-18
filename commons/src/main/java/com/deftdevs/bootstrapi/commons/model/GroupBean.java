package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean for groups REST requests.
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = BootstrAPI.GROUP)
public class GroupBean {

    @XmlElement
    private String name;

    @XmlElement
    private String description;

    @XmlElement
    private Boolean active;

    // Example instances for documentation and tests

    public static final GroupBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new GroupBean();
        EXAMPLE_1.setName("example");
        EXAMPLE_1.setDescription("Example Group");
        EXAMPLE_1.setActive(true);
    }

    public static final GroupBean EXAMPLE_2;

    static {
        EXAMPLE_2 = new GroupBean();
        EXAMPLE_2.setName("other");
        EXAMPLE_2.setDescription("Other Group");
        EXAMPLE_2.setActive(false);
    }

}
