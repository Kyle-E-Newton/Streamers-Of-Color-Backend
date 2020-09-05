/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamersOfColor.Util;

import org.springframework.beans.factory.annotation.Value;

/**
 * Variables read from Application.yml
 */
public class References {
    //@Value("${soc.url.CROSS_ORIGIN_URL}")
    public static final String CROSS_ORIGIN_URL = "http://localhost:3000";
    @Value("${soc.url.API_BASE_URL}")
    public static String API_BASE_URL;

    @Value("${soc.url.TWITCH_URL}")
    public static String TWITCH_URL;
    @Value("${soc.url.MIXER_URL}")
    public static String MIXER_URL;
    public static String YOUTUBE_URL = "https://youtube.com";
    public static String FACEBOOK_URL = "https://twitch.tv";

    @Value("${soc.url.TWITCH_CALLBACK_URL}")
    public static String TWITCH_CALLBACK;
    public static String TWITCH_CALLBACK_URL = API_BASE_URL + TWITCH_CALLBACK;
    @Value("${soc.url.TWITCH_SUBSCRIBE_STREAM_TOPIC_URL}")
    public static String TWITCH_SUBSCRIBE_STREAM_TOPIC_URL;

}
