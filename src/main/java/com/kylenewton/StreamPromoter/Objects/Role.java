/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamPromoter.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Role object to store in roles database
 */
@Document(collection = "roles")
public class Role {

    private ERole name;

    public Role() {

    }

    public Role(ERole role) {
        this.name = role;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }
}
