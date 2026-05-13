package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Model for users REST requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @Schema(description = "Group memberships keyed by group name. true ensures the user is a member, " +
            "false ensures the user is not a member, null is a no-op (leaves the membership untouched). " +
            "Group lifecycle is managed via the top-level groups map; referenced groups must already exist.")
    private Map<String, Boolean> groups;

    // Example instances for documentation and tests

    public static final UserModel EXAMPLE_1 = UserModel.builder()
        .username("example")
        .firstName("Example")
        .lastName("User")
        .fullName("Example User")
        .email("user@example.com")
        .active(true)
        .password("3x4mpl3")
        .build();

    public static final UserModel EXAMPLE_2 = UserModel.builder()
        .username("example")
        .firstName("Changed")
        .lastName("Example User")
        .fullName("Changed Example User")
        .active(false)
        .email("user@new-example.com")
        .build();

    public static final UserModel EXAMPLE_3_ADMIN = UserModel.builder()
        .firstName("Admini")
        .lastName("Strator")
        .fullName("Administrator")
        .username("admin")
        .email("admin@admin.de")
        .active(true)
        .password("admin")
        .build();

}
