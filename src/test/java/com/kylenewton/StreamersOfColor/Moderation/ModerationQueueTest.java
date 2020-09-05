/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamersOfColor.Moderation;

import com.kylenewton.StreamersOfColor.Objects.Stream;
import com.kylenewton.StreamersOfColor.Util.Platform;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModerationQueueTest {

    ModerationQueue mod_queue;
    Stream stream_to_add;

    @BeforeAll
    void setupAll() {
        mod_queue = new ModerationQueue();
        stream_to_add = new Stream("test_user", Platform.TWITCH);
    }

    @Test
    void addStreamToModerationQueueTest() {
        mod_queue.addStreamToModerationQueue(stream_to_add);
        assertEquals(stream_to_add, mod_queue.findByUsernameAndPlatform(stream_to_add.getUsername(), stream_to_add.getPlatform()));
    }

    @Test
    void removeStreamFromModerationQueueTest() {
        mod_queue.addStreamToModerationQueue(stream_to_add);
        mod_queue.removeStreamFromModerationQueue(stream_to_add.getUsername(), stream_to_add.getPlatform());
        assertNotEquals(stream_to_add, mod_queue.findByUsernameAndPlatform(stream_to_add.getUsername(), stream_to_add.getPlatform()));
    }

    @AfterAll()
    void cleanupAll() {
        mod_queue.emptyModerationQueue();
    }


}
