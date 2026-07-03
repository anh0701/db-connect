package com.anh.core.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.anh.model.ApplicationSetting;

public class ApplicationSettingMapper {

    private ApplicationSettingMapper() {
    }

    public static ApplicationSetting map(ResultSet rs) throws SQLException {

        ApplicationSetting connection = new ApplicationSetting();

        connection.key = rs.getString("key");
        connection.value = rs.getString("value");

        return connection;
    }
}
