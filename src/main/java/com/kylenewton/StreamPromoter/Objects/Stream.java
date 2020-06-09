/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamPromoter.Objects;

import com.kylenewton.StreamPromoter.Util.Platform;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.kylenewton.StreamPromoter.Util.Utils.print;

/**
 * Stream Object to hold all information about a stream/streamer
 */
@Document(collection = "streams")
public class Stream {

    private String username;
    private Platform platform;
    private String streamURL;
    private String userId;

    private String platformID;

    public Stream(String username, Platform platform) {
        this.username = username;
        this.platform = platform;
    }

    public Stream(String username, String platform) {
        this.username = username;
        this.platform = Platform.valueOf(platform.toUpperCase());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getStreamURL() {
        return streamURL;
    }

    public void setStreamURL(String streamURL) {
        this.streamURL = streamURL;
    }

    public String getPlatformID() {
        return platformID;
    }

    public void setPlatformID(String platformID) {
        this.platformID = platformID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]\n", this.username, this.platform);
    }

    public static Stream fromString(String stream) {
        if(stream.equals("[]")) {
            return null;
        }
        String[] tokens = stream.split(",");
        return new Stream(tokens[0].substring(1, tokens[0].length()), tokens[1].substring(1, tokens[1].length()-1));
    }
}
