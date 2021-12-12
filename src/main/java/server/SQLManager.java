package server;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.val;
import org.intellij.lang.annotations.Language;

import java.sql.*;


public class SQLManager {


    private static DataSource dataSource;
    public static Statement stat;

    public static synchronized void create(final String host, final String port, final String database, final String user, final String pass) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.zaxxer.hikari.HikariConfig");
        } catch (ClassNotFoundException e2) {
            util.Debug("driver mysql not found!");
            e2.printStackTrace();
            System.exit(0);
        }
        dataSource = new DataSource(host, port, database, user, pass);
    }

    public static void executeUpdate(@Language("SQL") String sql) throws SQLException {
//        util.Debug(sql);
        try (val conn = dataSource.getConnection()) {
            conn.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @FunctionalInterface
    public interface MyConsumer {
        void accept(ResultSet r) throws Exception;
    }

    public static void executeQuery(@Language("SQL") String query, MyConsumer consumer) {
        try (val conn = dataSource.getConnection()) {
            try {
                consumer.accept(conn.createStatement().executeQuery(query));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }


    public static class DataSource {
        private final HikariDataSource ds;

        public DataSource(final String host, final String port, final String database, final String user, final String pass) {
            HikariConfig config = new HikariConfig();
            val connectString ="jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false";
            System.out.println(connectString);
            config.setJdbcUrl(connectString);
            config.setUsername(user);
            config.setPassword(pass);
            config.setMaximumPoolSize(50);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.setConnectionTimeout(300_000);
            config.setConnectionTimeout(120_000);
            config.setLeakDetectionThreshold(300_000);
            config.setAutoCommit(true);
            ds = new HikariDataSource(config);
        }


        public Connection getConnection() throws SQLException {
            return ds.getConnection();
        }

        public void close() {
            util.Debug("CLOSE DATASOURCE");
            this.ds.close();
        }

    }


    public static synchronized boolean close() {
        util.Debug("Close connection to database");
        dataSource.close();
        return true;

    }
}
