/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamPromoter.Repository;

import com.kylenewton.StreamPromoter.Objects.Stream;
import com.kylenewton.StreamPromoter.Util.Platform;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Stream Repository
 */
public interface IStreamRepository extends MongoRepository<Stream, String> {
    Stream findByUsername(String username);
    Stream findByUsernameAndPlatform(String username, Platform platform);
    Stream deleteByUsernameAndPlatform(String username, Platform platform);
}
