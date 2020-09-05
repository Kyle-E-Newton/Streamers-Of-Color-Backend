/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamersOfColor.Controllers;

import com.kylenewton.StreamersOfColor.Objects.Report;
import com.kylenewton.StreamersOfColor.Repository.IReportRepository;
import com.kylenewton.StreamersOfColor.Response.ApiResponse;
import com.kylenewton.StreamersOfColor.Util.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kylenewton.StreamersOfColor.Util.References.CROSS_ORIGIN_URL;

/**
 * Report Controller
 * Handles all routes related to reports
 */
@CrossOrigin(origins = CROSS_ORIGIN_URL)
@RestController
public class ReportController {

    @Autowired
    IReportRepository reportRepository;

    /*
     * GET MAPPINGS
     *  - /report/all
     *      - Gets all Reports from the database
     */

    @GetMapping("/report/all")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    /*
     * POST MAPPINGS
     * - /report/new                               {username, platform, url}
     *      - Adds a new Report to the database, if a report for the streamer already exists, adds the URL to their existing report
     */

    /**
     * Creates or adds to a report
     * @param username  Reported username
     * @param platform  Platform of reported
     * @param url   Url of specific incident
     * @return  ApiResponse with success or failure
     */
    @PostMapping("/report/new")
    public ResponseEntity<?> createNewReport(@RequestHeader String username, @RequestHeader String platform, @RequestHeader String url) {
        Report exists = reportRepository.findByUsernameAndPlatform(username, Platform.valueOf(platform.toUpperCase()));
        //There is not an existing report
        if (exists == null) {
            Report newReport = new Report(username, Platform.valueOf(platform.toUpperCase()), url);
            reportRepository.save(newReport);
            return ResponseEntity.ok(new ApiResponse(true, String.format("Report added for Streamer %s on Platform %s", username, platform)));
        } else {
            Report existing = reportRepository.findByUsernameAndPlatform(username, Platform.valueOf(platform.toUpperCase()));
            existing.addIncidentURL(url);
            return ResponseEntity.ok(new ApiResponse(true, String.format("Incident url added to report for Streamer %s on Platform %s", username, platform)));
        }
    }
}
