/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamersOfColor.Repository;

import com.kylenewton.StreamersOfColor.Objects.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * User Repository
 */
public interface IUserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);

    boolean existsByUsername(String username);
}
