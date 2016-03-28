package de.flashbeatzz.bungeesystem;

import java.sql.*;

public class MySQL {

    private static Connection connection;
    private static String host, user, data, pass;
    private static Integer port;

    public MySQL() {
        host = Data.mysqlCfg.getString("MySQL.Host");
        user = Data.mysqlCfg.getString("MySQL.User");
        pass = Data.mysqlCfg.getString("MySQL.Password");
        data = Data.mysqlCfg.getString("MySQL.Database");
        port = Data.mysqlCfg.getInt("MySQL.Port");
    }

    public boolean closeConnection() {
        Data.console.info("Closing MySQL-Connection ...");
        try {
            connection.close();
            Data.console.info("Successfully closed.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean openConnection() {
        Data.console.info("Opening MySQL-Connection ...");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + data + "?autoReconnect=true", user, pass);
            Data.console.info("Successfully connected.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean execute(String query) {
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            return ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void update(String query) {
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet query(String query) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createTable(String name, String primary, String column) {
        Data.console.info("Creating MySQL-Table '" + name + "' ...");
        String columnString = primary == null ? column : column + ", PRIMARY KEY (`" + primary + "`)";

        update("CREATE TABLE IF NOT EXISTS `" + name + "` (" + columnString + ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
        Data.console.info("Successfully created.");
    }
}

