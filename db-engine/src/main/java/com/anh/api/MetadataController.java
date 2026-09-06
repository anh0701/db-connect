package com.anh.api;

import com.anh.core.metadata.MetadataService;
import com.anh.dto.ApiResponse;

import io.javalin.Javalin;

public class MetadataController {

    public static void register(
            Javalin app) {

        registerSchemas(app);

        registerTables(app);

        registerColumns(app);
    }

    private static void registerSchemas(
            Javalin app) {

        app.get(
            "/metadata/schemas/{sessionId}",
            ctx -> {

                try {

                    String sessionId = ctx.pathParam("sessionId");

                    ctx.json(ApiResponse.success(MetadataService.getSchemas(sessionId)));

                } catch (Exception e) {

                    e.printStackTrace();

                    ctx.status(500);

                    ctx.json(ApiResponse.error(500, e.getMessage()));
                }
            }
        );
    }

    private static void registerTables(
            Javalin app) {

        app.get(
            "/metadata/tables/{sessionId}",
            ctx -> {

                try {

                    String sessionId = ctx.pathParam("sessionId");

                    String schema = ctx.queryParam("schema");

                    ctx.json(ApiResponse.success(MetadataService.getTables(sessionId, schema)));

                } catch (Exception e) {

                    e.printStackTrace();

                    ctx.status(500);

                    ctx.json(ApiResponse.error(500, e.getMessage()));
                }
            }
        );
    }

    private static void registerColumns(
            Javalin app) {

        app.get(
            "/metadata/columns/{sessionId}/{schema}/{table}",
            ctx -> {

                try {

                    String sessionId = ctx.pathParam("sessionId");

                    String schema = ctx.pathParam("schema");

                    String table = ctx.pathParam("table");

                    ctx.json(
                            ApiResponse.success(
                                    MetadataService.getColumns(
                                            sessionId,
                                            schema,
                                            table)));

                } catch (Exception e) {

                    e.printStackTrace();

                    ctx.status(500);

                    ctx.json(ApiResponse.error(500, e.getMessage()));
                }
            }
        );
    }

}
