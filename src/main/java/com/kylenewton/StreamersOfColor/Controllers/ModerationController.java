/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamersOfColor.Controllers;

import com.kylenewton.StreamersOfColor.Integration.Twitch.TwitchIntegration;
import com.kylenewton.StreamersOfColor.Live.LiveList;
import com.kylenewton.StreamersOfColor.Moderation.ModerationQueue;
import com.kylenewton.StreamersOfColor.Objects.Report;
import com.kylenewton.StreamersOfColor.Objects.Role;
import com.kylenewton.StreamersOfColor.Objects.Stream;
import com.kylenewton.StreamersOfColor.Repository.IReportRepository;
import com.kylenewton.StreamersOfColor.Repository.IRoleRepository;
import com.kylenewton.StreamersOfColor.Repository.IStreamRepository;
import com.kylenewton.StreamersOfColor.Response.ApiResponse;
import com.kylenewton.StreamersOfColor.Util.Platform;
import com.kylenewton.StreamersOfColor.Util.References;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Moderation Controller
 * Handles all requests pertaining to moderation, addition or removal from the databases
 * Only accessible to Moderators and Admins
 */
@RestController
@CrossOrigin(origins = References.CROSS_ORIGIN_URL)
@RequestMapping("/moderation")
@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
public class ModerationController {

    @Autowired
    IStreamRepository streamRepository;

    @Autowired
    ModerationQueue moderationQueue;

    @Autowired
    IReportRepository reportRepository;

    @Autowired
    IRoleRepository roleRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    TwitchIntegration twitchIntegration;

    @Autowired
    LiveList liveList;

    /*
     * GET MAPPINGS
     *  - /moderation/all
     *      - Gets all approval requests
     */

    /**
     * Gets all Approval Requests from in memory Moderation Queue
     * @return  List of all approval requests
     */
    @GetMapping("/all")
    public List<Stream> getAllApprovalRequests() {
        return moderationQueue.getModerationQueue();
    }

    /*
     * POST MAPPINGS
     * - /moderation/approve                       {username, platform}
     *      - Approves streamer to be added to the main database
     * - /moderation/reject                        {username, platform}
     *      - Rejects streamer from the platform
     *          - Maybe keep a log of rejections to automate rejections of previously rejected applicants
     */

    /**
     * Approves a user from the moderation queue
     * @param username  Streamer Username
     * @param platform  Streamer Platform
     * @return  ApiResponse
     */
    @PostMapping("/approve")
    public ResponseEntity<?> approveStream(@RequestHeader String username, @RequestHeader String platform) {
        boolean modQueue = moderationQueue.moderationQueueContains(username, Platform.valueOf(platform.toUpperCase()));
        //User exists in the moderation queue
        if(modQueue) {
            Stream approved = moderationQueue.findByUsernameAndPlatform(username, Platform.valueOf(platform.toUpperCase()));
            moderationQueue.removeStreamFromModerationQueue(username, Platform.valueOf(platform.toUpperCase()));
            approved.setUserId(twitchIntegration.getUserIdFromUsername(username));
            streamRepository.save(approved);
            return ResponseEntity.ok(new ApiResponse(true, String.format("Approved Streamer %s on Platform %s", username, platform)));
        }
        return ResponseEntity.ok(new ApiResponse(false, String.format("Streamer %s on Platform %s was not in the moderation queue", username, platform)));
    }

    /**
     * Rejects a user from the moderation queue
     * @param username  Streamer Username
     * @param platform  Streamer Platform
     * @return  ApiResponse
     */
    @PostMapping("/reject")
    public ResponseEntity<?> rejectStream(@RequestHeader String username, @RequestHeader String platform) {
        boolean modQueue = moderationQueue.moderationQueueContains(username, Platform.valueOf(platform.toUpperCase()));
        //User exists in the moderation queue
        if(modQueue) {
            moderationQueue.removeStreamFromModerationQueue(username, Platform.valueOf(platform.toUpperCase()));
            return ResponseEntity.ok(new ApiResponse(true, String.format("Rejected Streamer %s on Platform %s", username, platform)));
        }
        return ResponseEntity.ok(new ApiResponse(false, String.format("Streamer %s on Platform %s was not in the moderation queue", username, platform)));
    }

    /*
     * DELETE MAPPINGS
     * - /moderation/stream                         {username, platform}
     *       - Removes Stream from the main database
     *       - Used to completely remove streamer from the platform
     *       - Removes any reports
     * - /moderation/report                         {username, platform}
     *       - Removes entire report from the application
     *       - Deletes all urls that show a complaint
     * - /moderation/url                            {username, platform, url}
     *       - Removes a single report url from the report, leaving the rest intact
     */

    /**
     * Removes Streamer from the platform
     * @param username  Streamer Username
     * @param platform  Streamer Platform
     * @return  ApiResponse
     */
    @DeleteMapping("/stream")
    public ResponseEntity<?> removeStream(@RequestHeader String username, @RequestHeader String platform) {
        streamRepository.deleteByUsernameAndPlatform(username, Platform.valueOf(platform.toUpperCase()));
        reportRepository.deleteByUsernameAndPlatform(username, Platform.valueOf(platform.toUpperCase()));
        liveList.removeStreamFromLiveList(username, Platform.valueOf(platform.toUpperCase()));
        //TODO: Store removed streams to blacklist
        return ResponseEntity.ok(new ApiResponse(true, String.format("Deleted Streamer %s on Platform %s from the database", username, platform)));
    }

    /**
     * Removes entire report from platform, including all incident urls
     * @param username  Streamer Username
     * @param platform  Streamer Platform
     * @return  ApiResponse
     */
    @DeleteMapping("/report")
    public ResponseEntity<?> removeReport(@RequestHeader String username, @RequestHeader String platform) {
        reportRepository.deleteByUsernameAndPlatform(username, Platform.valueOf(platform.toUpperCase()));
        return ResponseEntity.ok(new ApiResponse(true, String.format("Deleted Report for Streamer %s on Platform %s from the database", username, platform)));
    }

    /**
     * Removes a URL from a report
     * @param username  Streamer Username
     * @param platform  Streamer Platform
     * @param url   URL to remove
     * @return  ApiResponse
     */
    @DeleteMapping("/url")
    public ResponseEntity<?> removeUrl(@RequestHeader String username, @RequestHeader String platform, @RequestHeader String url) {
        Query query = new Query(where("username").is(username)).addCriteria(where("platform").is(Platform.valueOf(platform.toUpperCase())));
        Report exists = reportRepository.findByUsernameAndPlatform(username, Platform.valueOf(platform.toUpperCase()));
        exists.removeIncidentURL(url);
        mongoTemplate.updateFirst(query, Update.update("incidentURL", exists.getIncidentURL()), Report.class);
        return ResponseEntity.ok(new ApiResponse(true, String.format("Deleted incident url for Streamer %s on Platform %s", username, platform)));
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
