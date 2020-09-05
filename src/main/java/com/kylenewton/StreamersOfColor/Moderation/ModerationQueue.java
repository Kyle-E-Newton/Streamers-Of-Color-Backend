/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamersOfColor.Moderation;

import com.kylenewton.StreamersOfColor.Objects.Stream;
import com.kylenewton.StreamersOfColor.Util.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Holds all streams to be approved/rejected in memory for faster access
 */
@Component
public class ModerationQueue implements DisposableBean {

    private List<Stream> moderationQueue = new LinkedList<>();

    private final String filename = "ModerationQueue.txt";

    Logger logger = LoggerFactory.getLogger(ModerationQueue.class);

    public List<Stream> getModerationQueue() {
        return moderationQueue;
    }

    public void setModerationQueue(List<Stream> moderationQueue) {
        this.moderationQueue = moderationQueue;
    }

    /**
     * Adds stream to moderation queue
     * @param stream    Stream to add
     */
    public void addStreamToModerationQueue(Stream stream) {
        this.moderationQueue.add(stream);
    }

    /**
     * Removes stream from moderation queue
     * @param username  Streamer username
     * @param platform  Streamer platform
     */
    public void removeStreamFromModerationQueue(String username, Platform platform) {
        //If username and platform are a match, then remove
        this.moderationQueue.removeIf(stream -> stream.getUsername().equals(username) && stream.getPlatform().equals(platform));
    }

    public void emptyModerationQueue() {
        this.moderationQueue.clear();
    }

    /**
     * Checks if the streamer is already in the moderation queue
     * @param username  Streamer username
     * @param platform  Streamer platform
     * @return  true if found, false if not
     */
    public boolean moderationQueueContains(String username, Platform platform) {
        for(Stream stream: this.moderationQueue) {
            if(stream.getUsername().equals(username) && stream.getPlatform().equals(platform)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a stream from the moderation queue by username/platform
     * @param username  Streamer username
     * @param platform  Streamer platform
     * @return  Stream object
     */
    public Stream findByUsernameAndPlatform(String username, Platform platform) {
        for(Stream stream: this.moderationQueue) {
            if(stream.getUsername().equals(username) && stream.getPlatform().equals(platform)) {
                return stream;
            }
        }
        return null;
    }

    @PostConstruct
    private void init() throws IOException {
        try {
            File f = new File(filename);
            f.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            Stream temp;
            String line = reader.readLine();
            while (line != null) {
                temp = Stream.fromString(line);
                line = reader.readLine();
                if (temp == null) {
                    continue;
                }
                this.moderationQueue.add(temp);
            }
            reader.close();
        } catch (IOException e) {

        }
    }

    @Override
    public void destroy() throws IOException {
        logger.info("Writing Moderation Queue to Disk");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for(Stream stream: this.moderationQueue) {
            writer.write(stream.toString());
        }
        writer.close();
        logger.info("Finished Writing Moderation Queue to Disk");
    }
}
