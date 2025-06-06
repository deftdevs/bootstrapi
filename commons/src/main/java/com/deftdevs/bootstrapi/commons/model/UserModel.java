package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Model for users REST requests.
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = BootstrAPI.USER)
public class UserModel {

    @XmlElement
    private String username;

    @XmlElement
    private String firstName;

    @XmlElement
    private String lastName;

    @XmlElement
    private String fullName;

    @XmlElement
    private String email;

    @XmlElement
    private Boolean active;

    @XmlElement
    @EqualsAndHashCode.Exclude
    private String password;

    @XmlElement
    private List<GroupModel> groups;

    // Example instances for documentation and tests

    public static final UserModel EXAMPLE_1;

    static {
        EXAMPLE_1 = new UserModel();
        EXAMPLE_1.setUsername("example");
        EXAMPLE_1.setFirstName("Example");
        EXAMPLE_1.setLastName("User");
        EXAMPLE_1.setFullName("Example User");
        EXAMPLE_1.setEmail("user@example.com");
        EXAMPLE_1.setActive(true);
        EXAMPLE_1.setPassword("3x4mpl3");
    }

    public static final UserModel EXAMPLE_2;

    static {
        EXAMPLE_2 = new UserModel();
        EXAMPLE_2.setUsername("example");
        EXAMPLE_2.setFirstName("Changed");
        EXAMPLE_2.setLastName("Example User");
        EXAMPLE_2.setFullName("Changed Example User");
        EXAMPLE_2.setActive(false);
        EXAMPLE_2.setEmail("user@new-example.com");
    }

    public static final UserModel EXAMPLE_3_ADMIN;

    static {
        EXAMPLE_3_ADMIN = new UserModel();
        EXAMPLE_3_ADMIN.setFirstName("Admini");
        EXAMPLE_3_ADMIN.setLastName("Strator");
        EXAMPLE_3_ADMIN.setFullName("Administrator");
        EXAMPLE_3_ADMIN.setUsername("admin");
        EXAMPLE_3_ADMIN.setEmail("admin@admin.de");
        EXAMPLE_3_ADMIN.setActive(true);
        EXAMPLE_3_ADMIN.setPassword("admin");
    }
}
