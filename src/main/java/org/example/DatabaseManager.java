package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseManager {
    public static final String DB_URL = System.getenv("DB_URL");
    public static final String DB_User = System.getenv("DB_USER");
    public static final String DB_Password = System.getenv("DB_PASSWORD");

    public static Map<String, String> getAllUsers() {
        // Используем LinkedHashMap, чтобы сохранить порядок вывода из базы
        Map<String, String> users = new LinkedHashMap<>();
        String sql = "SELECT login, name FROM app";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_User, DB_Password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Пробегаемся по всем найденным пользователям и складываем их в словарь
            while (rs.next()) {
                String login = rs.getString("login");
                String fullName = rs.getString("name");
                users.put(login, fullName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static String getUserWithLangs(String loginInput){
        //составляем запрос на соединение таблиц по ключевым элементам
        String sql = "Select u.name, l.name as lang_name " +
                "From app u " + "left join language_app la on u.id = la.app_id " +
                "left join languages l on la.language_id = l.id " + "Where u.login = ? ";
        //инструмент для склейки стрингов
        StringBuilder result = new StringBuilder();
        //создение подключение и подготовка запроса
        try(Connection conn = DriverManager.getConnection(DB_URL,DB_User,DB_Password);
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            //подставляем логин в ? и убираем пробелы
            pstmt.setString(1,loginInput.trim());
            //выполняем запрос и получаем ответ
            ResultSet rs = pstmt.executeQuery();

            boolean found = false;

            while(rs.next()){
                if(!found){
                    result.append("имя: ").append(rs.getString("name"))
                            .append("\nязыки: ");
                    found = true;
                }
                else{
                    result.append(", ");
                }

                String lang = rs.getString("lang_name");

                if(lang!=null){
                    result.append(lang);
                }
            }
            if(!found)return "такого пользователя не существует";
        }
        catch (SQLException e){
            e.printStackTrace();
            return "ошибка при работе с базой данных";
        }
        return result.toString();
    }
}
