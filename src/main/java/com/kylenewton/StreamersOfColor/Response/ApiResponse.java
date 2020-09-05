/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */
package com.kylenewton.StreamersOfColor.Response;

/**
 * API response with success or failure
 */
public class ApiResponse {
    private boolean success;
    private String message;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
