package com.deftdevs.bootstrapi.confluence.atlassian.user;

import com.atlassian.user.User;
import lombok.Setter;

@Setter
public class UserImpl implements User {

    private String name;
    private String fullName;
    private String email;

    public UserImpl(
            final User user) {

        this.name = user.getName();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getEmail() {
        return email;
    }

}
