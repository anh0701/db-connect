package com.anh.api;

import com.anh.model.ApplicationSetting;
import com.anh.model.SavedConnection;
import com.anh.repository.ApplicationSettingRepository;

import io.javalin.Javalin;

public class ApplicationSettingController {

    private static final ApplicationSettingRepository repository =
            new ApplicationSettingRepository();

    public static void register(Javalin app) {

        registerGet(app);

        registerCreate(app);

        registerUpdate(app);

        // registerDelete(app);
    }

    private static void registerUpdate(Javalin app) {
        app.put("/settings", ctx -> {
            ApplicationSetting request =
                    ctx.bodyAsClass(ApplicationSetting.class);

            int id = repository.update(request.key, request.value);

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
