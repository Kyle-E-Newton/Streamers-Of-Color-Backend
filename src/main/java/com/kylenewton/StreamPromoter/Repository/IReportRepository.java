/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamPromoter.Repository;

import com.kylenewton.StreamPromoter.Objects.Report;
import com.kylenewton.StreamPromoter.Util.Platform;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Report Repository
 */
public interface IReportRepository extends MongoRepository<Report, String> {
    Report findByUsernameAndPlatform(String username, Platform platform);
    Report deleteByUsernameAndPlatform(String username, Platform platform);
    Report deleteByUsername(String username);
}
