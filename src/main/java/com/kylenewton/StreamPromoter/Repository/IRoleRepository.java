/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamPromoter.Repository;

import com.kylenewton.StreamPromoter.Objects.ERole;
import com.kylenewton.StreamPromoter.Objects.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Role Repository
 */
public interface IRoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
