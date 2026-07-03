package com.anh.api;

import com.anh.model.ApplicationSetting;
import com.anh.repository.ApplicationSettingRepository;

import io.javalin.Javalin;

public class ApplicationSettingController {

    private static final ApplicationSettingRepository repository =
            new ApplicationSettingRepository();

    public static void register(Javalin app) {

        registerGet(app);

        registerCreate(app);

        registerUpdate(app);

        registerGetByKey(app);

        // registerDelete(app);
    }

    private static void registerGetByKey(Javalin app) {
        app.get("/settings/{key}", ctx -> {

            String key = ctx.pathParam("key");

            ctx.json(repository.findByKey(key));
        });
    }

    private static void registerUpdate(Javalin app) {
        app.put("/settings/{key}", ctx -> {
            ApplicationSetting request =
                    ctx.bodyAsClass(ApplicationSetting.class);

            String key = ctx.pathParam("key");

            int id = repository.update(key, request.value);

            ctx.status(201);

            ctx.json(id);
        });
    }

    private static void registerCreate(Javalin app) {

    }

    private static void registerGet(Javalin app) {
        app.get("/settings", ctx -> {
            ctx.json(repository.findAll());
        });
    }
}
