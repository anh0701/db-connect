package com.anh.api;

import com.anh.dto.ResponseUtil;
import com.anh.model.SavedConnection;
import com.anh.repository.SavedConnectionRepository;

import io.javalin.Javalin;

public class SavedConnectionController {

    private static final SavedConnectionRepository repository =
            new SavedConnectionRepository();

    public static void register(Javalin app) {

        registerList(app);

        registerGet(app);

        registerCreate(app);

        registerUpdate(app);

        registerDelete(app);
    }

    private static void registerGet(Javalin app) {
        app.get("/saved-connections/{id}", ctx -> {

            int id = Integer.parseInt(
                    ctx.pathParam("id"));

            SavedConnection connection =
                    repository.findById(id);

            if (connection == null) {

                ResponseUtil.error(
                        ctx,
                        404,
                        "Saved connection not found");

                return;
            }

            ResponseUtil.success(
                ctx,
                200,
                "Success",
                connection);

        });

    }

    private static void registerDelete(Javalin app) {
        app.delete("/saved-connections/{id}", ctx -> {
            int id = Integer.parseInt(
                    ctx.pathParam("id"));

            int res = repository.delete(id);

            ResponseUtil.success(
                ctx,
                201,
                "Success",
                res);
        });
    }

    private static void registerUpdate(Javalin app) {
        app.put("/saved-connections/{id}", ctx -> {
            int id = Integer.parseInt(
                    ctx.pathParam("id"));

            SavedConnection request =
                    ctx.bodyAsClass(SavedConnection.class);
            int res = repository.update(request.name, request.databaseType,
                request.host, request.port, request.databaseName, request.username, id);

            ResponseUtil.success(
                ctx,
                201,
                "Success",
                res);
        });
    }

    private static void registerCreate(Javalin app) {

        app.post("/saved-connections", ctx -> {

            SavedConnection request =
                    ctx.bodyAsClass(SavedConnection.class);

            int id = repository.save(request);

            ResponseUtil.success(
                ctx,
                201,
                "Success",
                id);
        });
    }

    private static void registerList(Javalin app) {
        app.get("/saved-connections", ctx -> {

            ResponseUtil.success(
                ctx,
                201,
                "Success",
                repository.findAll());

        });
    }

}
