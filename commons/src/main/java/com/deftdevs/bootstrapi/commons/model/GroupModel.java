package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model for groups REST requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = BootstrAPI.GROUP)
public class GroupModel {

    @XmlElement
    private String name;

    @XmlElement
    private String description;

    @XmlElement
    private Boolean active;

    // Example instances for documentation and tests

    public static final GroupModel EXAMPLE_1 = GroupModel.builder()
        .name("example")
        .description("Example Group")
        .active(true)
        .build();

    public static final GroupModel EXAMPLE_2 = GroupModel.builder()
        .name("other")
        .description("Other Group")
        .active(false)
        .build();

}
