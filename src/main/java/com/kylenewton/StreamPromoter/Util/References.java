/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamPromoter.Util;

/**
 * Variables read from Application.yml
 */
public class References {
    public static final String CROSS_ORIGIN_URL = "${soc.url.CROSS_ORIGIN_URL}";
    public static final String API_BASE_URL = "${soc.url.API_BASE_URL}";

    public static final String TWITCH_URL = "${soc.url.TWITCH_URL}";
    public static final String MIXER_URL = "${soc.url.MIXER_URL}";
    public static final String YOUTUBE_URL = "https://youtube.com";
    public static final String FACEBOOK_URL = "https://twitch.tv";

    public static final String TWITCH_CALLBACK_URL = API_BASE_URL + "${soc.url.TWITCH_CALLBACK_URL}";
    public static final String TWITCH_SUBSCRIBE_STREAM_TOPIC_URL = "${soc.url.TWITCH_SUBSCRIBE_STREAM_TOPIC_URL}";

}
