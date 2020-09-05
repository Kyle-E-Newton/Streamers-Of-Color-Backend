/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamersOfColor.Controllers;

import com.kylenewton.StreamersOfColor.Live.LiveList;
import com.kylenewton.StreamersOfColor.Repository.IStreamRepository;
import com.kylenewton.StreamersOfColor.Request.TwitchStreamChangedRequest;
import com.kylenewton.StreamersOfColor.Util.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Twitch Controller
 * Handles redirect routes from Twitch
 * Still needs testing (TODO)
 * Theoretically adds to and removes from the live list
 */
@RestController
@CrossOrigin
public class TwitchController {

    @Autowired
    LiveList liveList;

    @Autowired
    IStreamRepository streamRepository;

    @GetMapping("/callback/twitch")
    public ResponseEntity<?> userWentLive(@RequestBody TwitchStreamChangedRequest streamChangedRequest) {
        processUser(streamChangedRequest);
        return ResponseEntity.ok(200);
    }

    @Async
    public void processUser(TwitchStreamChangedRequest streamChangedRequest) {
        if(streamChangedRequest.getType().equals("live")) {
            //Add to Live List
            liveList.addStreamToLiveList(streamRepository.findByUsernameAndPlatform(streamChangedRequest.getUser_name(), Platform.TWITCH));
        } else {
            //Remove from Live List
            liveList.removeStreamFromLiveList(streamChangedRequest.getUser_name(), Platform.TWITCH);
        }
    }
}
