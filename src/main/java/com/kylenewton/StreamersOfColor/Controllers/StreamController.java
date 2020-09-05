/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamersOfColor.Controllers;


import com.kylenewton.StreamersOfColor.Live.LiveList;
import com.kylenewton.StreamersOfColor.Moderation.ModerationQueue;
import com.kylenewton.StreamersOfColor.Objects.Stream;
import com.kylenewton.StreamersOfColor.Repository.IStreamRepository;
import com.kylenewton.StreamersOfColor.Response.ApiResponse;
import com.kylenewton.StreamersOfColor.Util.Platform;
import com.kylenewton.StreamersOfColor.Util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kylenewton.StreamersOfColor.Util.References.CROSS_ORIGIN_URL;

/**
 * Stream Controller
 * Handles most of the front end requests
 * Allows getting from and sending to databases and queues
 */
@CrossOrigin(origins = CROSS_ORIGIN_URL)
@RestController
public class StreamController {

    @Autowired
    IStreamRepository streamRepository;

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ModerationQueue moderationQueue;

    @Autowired
    LiveList liveList;

    //Logger
    Logger logger = LoggerFactory.getLogger(StreamController.class);
    /*
     * GET MAPPINGS
     *  - /stream/all
     *      - Gets all Streams in the database
     *  - /stream/random                            {number}
     *      - Gets n random Streams from the database
     */

    /**
     * Returns all streams in the database
     * @return List of Streams
     */
    @GetMapping("/stream/all")
    public List<Stream> getAllStreams() {
        return streamRepository.findAll();
    }

    /**
     * Returns n random streams from the database
     * @param n How many random streams to return
     * @return List of Streams
     */
    @GetMapping("/stream/random")
    public List<Stream> getRandomStreams(@RequestHeader int n) {
//        SampleOperation stage = Aggregation.sample(n);
//        Aggregation aggregation = Aggregation.newAggregation(stage);
//        AggregationResults<Stream> output = mongoTemplate.aggregate(aggregation, "streams", Stream.class);
//        return output.getMappedResults();
        return liveList.getNStreams(n);
    }

    /*
     * POST MAPPINGS
     * - /stream/new                               {username, platform}
     *      - Adds a new stream to the database
     */

    /**
     * Adds a new stream to the moderation queue
     * @param username streamer username
     * @param platform streamer platform (twitch/mixer/etc.)
     * @return ApiResponse with success or failure
     */
    @PostMapping("/stream/new")
    public ResponseEntity<?> addNewStream(@RequestHeader String username, @RequestHeader String platform) {
        Stream exists = streamRepository.findByUsernameAndPlatform(username, Platform.valueOf(platform.toUpperCase()));
        boolean moderation = moderationQueue.moderationQueueContains(username, Platform.valueOf(platform.toUpperCase()));
        //If the stream does not exist in the streamRepository or the moderation queue
        if(exists == null && !moderation) {
            Stream newStream = new Stream(username, Platform.valueOf(platform.toUpperCase()));
            newStream = Utils.populateURL(newStream);
            moderationQueue.addStreamToModerationQueue(newStream);
            return ResponseEntity.ok(new ApiResponse(true, String.format("Added Streamer %s on Plaform %s to the moderation Queue", username, platform)));
        }
        //Stream exists in streamRepository
        else if (exists != null && !moderation) {
            return ResponseEntity.ok(new ApiResponse(false, String.format("Streamer %s on Platform %s is already in the database", username, platform)));
        } else {
            return ResponseEntity.ok(new ApiResponse(false, String.format("Streamer %s on Platform %s is already in the moderation Queue", username, platform)));
        }
    }
}
