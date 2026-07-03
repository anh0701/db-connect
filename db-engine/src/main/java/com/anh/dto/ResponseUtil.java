package com.anh.dto;

import io.javalin.http.Context;

public class ResponseUtil {

    public static <T> void success(
            Context ctx,
            int status,
            String message,
            T data) {

        ctx.status(status);
        ctx.json(new ApiResponse<>(status, message, data));
    }

    public static void error(
            Context ctx,
            int status,
            String message) {

        ctx.status(status);
        ctx.json(new ApiResponse<>(status, message, null));
    }
}
