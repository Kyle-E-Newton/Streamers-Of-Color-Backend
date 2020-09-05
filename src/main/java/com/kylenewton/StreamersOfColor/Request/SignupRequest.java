/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamersOfColor.Request;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * Signup request
 * Matches frontend request structure
 */
public class SignupRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;


    private Set<String> roles;

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
