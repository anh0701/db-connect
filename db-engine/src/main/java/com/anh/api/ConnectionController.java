package com.anh.api;

import com.anh.core.connection.ConnectionManager;
import com.anh.dto.ApiResponse;
import com.anh.dto.ConnectionRequest;
import com.anh.dto.ConnectionResponse;

import io.javalin.Javalin;

public class ConnectionController {
    public static void register(Javalin app) {

        registerTest(app);

        registerCreate(app);

        registerClose(app);

        registerList(app);
    }

    private static void registerTest(Javalin app) {

        app.post("/connect/test", ctx -> {

            try {

                ConnectionRequest request = ctx.bodyAsClass(ConnectionRequest.class);

                ConnectionManager.testConnection(request);

                ConnectionResponse response = new ConnectionResponse();
                response.success = true;
                response.message = "Connection successful";

                ctx.json(ApiResponse.success(response));

            } catch (Exception e) {

                e.printStackTrace();

                ctx.status(500);

                ctx.json(ApiResponse.error(500, e.getMessage()));
            }
        });
    }

    private static void registerCreate(Javalin app) {

        app.post("/connect/create", ctx -> {

            try {

                ConnectionRequest request = ctx.bodyAsClass(ConnectionRequest.class);

                String sessionId = ConnectionManager.createConnection(request);

                ConnectionResponse response = new ConnectionResponse();
                response.success = true;
                response.message = "Session created";
                response.sessionId = sessionId;

                ctx.status(201);
                ctx.json(ApiResponse.created(response));

            } catch (Exception e) {

                e.printStackTrace();

                ctx.status(500);

                ctx.json(ApiResponse.error(500, e.getMessage()));
            }
        });
    }

    private static void registerClose(Javalin app) {

        app.delete("/connect/{sessionId}", ctx -> {

            try {

                String sessionId = ctx.pathParam("sessionId");

                ConnectionManager.closeSession(sessionId);

                ConnectionResponse response = new ConnectionResponse();
                response.success = true;
                response.message = "Session closed";
                response.sessionId = sessionId;

                ctx.json(ApiResponse.success(response));

            } catch (Exception e) {

                e.printStackTrace();

                ctx.status(500);

                ctx.json(ApiResponse.error(500, e.getMessage()));
            }
        });
    }

    private static void registerList(Javalin app) {

        app.get("/connect/sessions", ctx -> {

            ctx.json(
                    ApiResponse.success(
                            ConnectionManager.getSessionInfos()));
        });
    }
}
