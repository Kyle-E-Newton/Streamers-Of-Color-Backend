/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamersOfColor.Objects;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Moderator Object
 * Not really used
 */
@Document(collection = "moderators")
public class Moderator extends User{
}
