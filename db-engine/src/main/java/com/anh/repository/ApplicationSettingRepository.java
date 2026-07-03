package com.anh.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.anh.core.mapper.ApplicationSettingMapper;
import com.anh.model.ApplicationSetting;

public class ApplicationSettingRepository extends BaseRepository{

    public List<ApplicationSetting> findAll(){
        String sql = """
                Select * from application_setting;
                """;
        List<ApplicationSetting> settings = new ArrayList<>();

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    settings.add(ApplicationSettingMapper.map(rs));
                }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return settings;
    }

    public int update (String key, String value){
        String sql = """
                update application_setting set value = ? where key = ?
                """;
        try (
            Connection dbConnection = getConnection();
            PreparedStatement statement = dbConnection.prepareStatement(
                sql
            )) {

            statement.setString(1, value);
            statement.setString(2, key);

            return statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
