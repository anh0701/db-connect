package com.anh.api;

import io.javalin.Javalin;

public class HealthController {
     public static void register(Javalin app) {

        app.get("/ping", ctx -> {

            ctx.result("pong");
        });
    }
}
