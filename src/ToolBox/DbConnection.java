package ToolBox;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class DbConnection {
    public static Connection connection = null;
    public static Statement stmt = null;
    public static String dbName = "jetTunesData.sqlite";

    public static void main(String[] args) {
        try {
            createConnection("JetTunes");
            stmt = connection.createStatement();
            dropTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createConnection(String filePath) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            File directory = new File(filePath);
            if (!directory.exists()) {
                directory.mkdir();
            }
            String pathTillProject = System.getProperty("user.dir");
            connection = DriverManager.getConnection("jdbc:sqlite:" + pathTillProject + "/" + filePath + "/" + dbName);
            System.out.println("Opened database successfully");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void createTable(String name) {
        try {
            stmt = connection.createStatement();
            // Creating Table
            String sql = "CREATE TABLE IF NOT EXISTS " + name + " " +
                    "(songName TEXT)";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getMusicList() {

        ArrayList<String> musicList = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("Select * from musicList");
            while (rs.next())
                musicList.add(rs.getString("songName"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return musicList;
    }

    public static void addSong(String tableName, String songName) {
        try {
            songName = songName.replaceAll("'", "`");
            String sql = "INSERT INTO " + tableName + " (songName) " +
                    "VALUES ('" + songName + "');";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteSong(String tableName, String songName) {
        try {
            String sql = "DELETE FROM " + tableName + " WHERE songName = '" + songName + "';";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void dropTable() {
        try {
            String sql = "Drop table if exists musicList;";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        createTable("musicList");
    }

}


