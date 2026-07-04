package com.anh.api;

import com.anh.dto.ApiResponse;

import io.javalin.Javalin;

public class HealthController {
     public static void register(Javalin app) {

        app.get("/ping", ctx -> {

            ctx.json(ApiResponse.success("pong"));
        });
    }
}
