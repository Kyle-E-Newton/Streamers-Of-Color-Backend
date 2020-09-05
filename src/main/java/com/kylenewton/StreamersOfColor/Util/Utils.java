/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamersOfColor.Util;

import com.kylenewton.StreamersOfColor.Objects.Stream;

import static com.kylenewton.StreamersOfColor.Util.References.TWITCH_URL;

/**
 * Holds Misc. Utility Functions
 */
public class Utils {

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
