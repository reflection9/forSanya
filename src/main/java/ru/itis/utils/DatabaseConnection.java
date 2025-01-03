package ru.itis.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public final class DatabaseConnection {

    private static final String PROPERTIES_FILE = "db.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Драйвер PostgreSQL загружен.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Не удалось загрузить драйвер PostgreSQL", e);
        }

        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Не найден файл конфигурации базы данных: " + PROPERTIES_FILE);
            }
            PROPERTIES.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке конфигурации базы данных", e);
        }
    }

    private DatabaseConnection() {}

    public static Connection getConnection() {
        try {
            String url = PROPERTIES.getProperty("db.url");
            String username = PROPERTIES.getProperty("db.username");
            String password = PROPERTIES.getProperty("db.password");
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных: " + e.getMessage(), e);
        }
    }
}
