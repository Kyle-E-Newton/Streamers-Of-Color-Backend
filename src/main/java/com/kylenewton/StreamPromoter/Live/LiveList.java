/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamPromoter.Live;

import com.kylenewton.StreamPromoter.Objects.Stream;
import com.kylenewton.StreamPromoter.Util.Platform;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Holds the live channels in memory for faster querying of random streams
 */
@Configuration
public class LiveList {

    private List<Stream> liveStreams = new LinkedList<>();

    public List<Stream> getLiveStreams() {
        return liveStreams;
    }

    public void setLiveStreams(List<Stream> liveStreams) {
        this.liveStreams = liveStreams;
    }

    /**
     * Adds a Stream to the live list
     * @param stream    Stream to add
     */
    public void addStreamToLiveList(Stream stream) {
        this.liveStreams.add(stream);
    }

    /**
     * Removes a stream from the live list
     * @param username  Streamer Username
     * @param platform  Streamer Platform
     */
    public void removeStreamFromLiveList(String username, Platform platform) {
        this.liveStreams.removeIf(stream -> stream.getUsername().equals(username) && stream.getPlatform().equals(platform));
    }

    /**
     * Gets n random streams from the list
     * @param n How many streams to return
     * @return  Shuffled list of n Streams
     */
    public List<Stream> getNStreams(int n) {
        Collections.shuffle(this.liveStreams);
        if(liveStreams.size() < n) {
            return this.liveStreams;
        }
        return this.liveStreams.subList(0, n);
    }
}
