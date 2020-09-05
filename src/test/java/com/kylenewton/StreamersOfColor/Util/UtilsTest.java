/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamersOfColor.Util;

import com.kylenewton.StreamersOfColor.Objects.Stream;
import org.junit.jupiter.api.Test;

import static com.kylenewton.StreamersOfColor.Util.Utils.populateURL;
import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void testPopulateURL() {
        Stream twitch_stream = new Stream("test_user", Platform.TWITCH);
        Stream twitch_populated = populateURL(twitch_stream);
        assertEquals("https://twitch.tv/test_user", twitch_populated.getStreamURL());
    }
}
