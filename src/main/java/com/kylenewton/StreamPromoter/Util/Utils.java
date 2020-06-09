/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamPromoter.Util;

import com.kylenewton.StreamPromoter.Objects.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

import static com.kylenewton.StreamPromoter.Util.References.MIXER_URL;
import static com.kylenewton.StreamPromoter.Util.References.TWITCH_URL;

/**
 * Holds Misc. Utility Functions
 */
public class Utils {

    /**
     * Populates all streams in a list with their respective URLS
     * @param streams   Streams to populate
     * @return          List of Streams with populated stream urls
     */
    public static List<Stream> populateURL(List<Stream> streams) {
        for(Stream stream: streams) {
            populateURL(stream);
        }
        return streams;
    }

    /**
     * Populates a single Stream url
     * @param stream    Stream to populate
     * @return          Stream object that is populated
     */
    public static Stream populateURL(Stream stream) {
        Platform platform = stream.getPlatform();
        switch (platform) {
            case TWITCH:
                stream.setStreamURL(TWITCH_URL + "/" + stream.getUsername());
                break;
            case MIXER:
                stream.setStreamURL(MIXER_URL + "/" + stream.getUsername());

                break;
            default:
                stream.setStreamURL(TWITCH_URL);
        }
        return stream;
    }

    public static void print(Object ...o) {
        for (Object obj: o)
            System.out.println(obj);
    }
}
