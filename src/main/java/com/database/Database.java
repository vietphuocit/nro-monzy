package com.database;

import com.database.result.MonzyResultSet;
import com.database.result.MonzyResultSetImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Properties;

@Slf4j
public class Database {

    private static final String CONFIG_FILE_PATH = "data/config/monzy.properties";
    private static final String DATABASE_DRIVER_KEY = "monzy.database.driver";
    private static final String DATABASE_HOST_KEY = "monzy.database.host";
    private static final String DATABASE_PORT_KEY = "monzy.database.port";
    private static final String DATABASE_NAME_KEY = "monzy.database.name";
    private static final String DATABASE_USER_KEY = "monzy.database.user";
    private static final String DATABASE_PASSWORD_KEY = "monzy.database.pass";
    private static final String DATABASE_MIN_CONN_KEY = "monzy.database.min";
    private static final String DATABASE_MAX_CONN_KEY = "monzy.database.max";
    private static final String DATABASE_LIFETIME_KEY = "monzy.database.lifetime";
    private static final String DATABASE_LOG_QUERY_KEY = "monzy.database.log";
    private static String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://%s:%s/%s?useUnicode=yes&characterEncoding=UTF-8";
    private static String DB_HOST = "localhost";
    private static String DB_PORT = "3306";
    private static String DB_NAME = "monzy";
    private static String DB_USER = "root";
    private static String DB_PASSWORD = "";
    private static int MIN_CONN = 1;
    private static int MAX_CONN = 1;
    private static long MAX_LIFE_TIME = 120000L;
    public static boolean LOG_QUERY = false;
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    public Database() {
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void close() {
        ds.close();
    }

    private static void loadProperties() {
        try (InputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            Properties properties = new Properties();
            properties.load(input);
            DRIVER = properties.getProperty(DATABASE_DRIVER_KEY);
            DB_HOST = properties.getProperty(DATABASE_HOST_KEY);
            DB_PORT = properties.getProperty(DATABASE_PORT_KEY);
            DB_NAME = properties.getProperty(DATABASE_NAME_KEY);
            DB_USER = properties.getProperty(DATABASE_USER_KEY);
            DB_PASSWORD = properties.getProperty(DATABASE_PASSWORD_KEY);
            MIN_CONN = Integer.parseInt(properties.getProperty(DATABASE_MIN_CONN_KEY));
            MAX_CONN = Integer.parseInt(properties.getProperty(DATABASE_MAX_CONN_KEY));
            MAX_LIFE_TIME = Long.parseLong(properties.getProperty(DATABASE_LIFETIME_KEY));
            LOG_QUERY = Boolean.parseBoolean(properties.getProperty(DATABASE_LOG_QUERY_KEY));
            log.info("Load file properites thành công!");
        } catch (IOException | NumberFormatException e) {
            log.error(e.getMessage());
        }
    }

    public static MonzyResultSet executeQuery(String query) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery()) {
            if (LOG_QUERY) {
                log.info("Thực thi thành công câu lệnh: " + stmt);
                Log.getInstance().log(stmt.toString());
            }
            try {
                return new MonzyResultSetImpl(rs);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (SQLException e) {
            log.error("Có lỗi xảy ra khi thực thi câu lệnh: " + query);
            throw e;
        }
    }

    public static MonzyResultSet executeQuery(String query, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            if (LOG_QUERY) {
                log.info("Thực thi thành công câu lệnh: " + stmt.toString());
                Log.getInstance().log(stmt.toString());
            }
            ResultSet rs = stmt.executeQuery();
            try {
                return new MonzyResultSetImpl(rs);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (SQLException e) {
            log.error("Có lỗi xảy ra khi thực thi câu lệnh: " + query);
            throw e;
        }
    }

    public static int executeUpdate(String query) throws Exception {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            if (LOG_QUERY) {
                log.info("Thực thi thành công câu lệnh: " + ps.toString());
                Log.getInstance().log(ps.toString());
            }
            return ps.executeUpdate();
        }
    }

    public static int executeUpdate(String query, Object... objs) throws Exception {
        if (query.startsWith("insert") && query.endsWith("()")) {
            String placeholders = String.join(",", Collections.nCopies(objs.length, "?"));
            query = query.replace("()", "(" + placeholders + ")");
        }
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            for (int i = 0; i < objs.length; ++i) {
                ps.setObject(i + 1, objs[i]);
            }
            if (LOG_QUERY) {
                log.info("Thực thi thành công câu lệnh: " + ps.toString());
                Log.getInstance().log(ps.toString());
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            log.error("Có lỗi xảy ra khi thực thi câu lệnh: " + query);
            throw e;
        }
    }

    static {
        loadProperties();
        config.setDriverClassName(DRIVER);
        config.setJdbcUrl(String.format(URL, DB_HOST, DB_PORT, DB_NAME));
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setMinimumIdle(MIN_CONN);
        config.setMaximumPoolSize(MAX_CONN);
        config.setMaxLifetime(MAX_LIFE_TIME);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "true");
        ds = new HikariDataSource(config);
    }
}

