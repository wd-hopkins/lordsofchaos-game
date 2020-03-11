package org.lordsofchaos.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class DatabaseCommunication {

    /*
    Database information:
    dbname= vUx0GmhOrL, username=vUx0GmhOrL, pw= uKKJhxJLlm, server= remotemysql.com, port 3306
     */

    private static String dbName = "vUx0GmhOrL";
    private static String dbURL = "jdbc:mysql://remotemysql.com:3306/vUx0GmhOrL?useSSL=false&useUnicode=true&characterEncoding=UTF-8&user=vUx0GmhOrL&password=uKKJhxJLlm";

    // count is the number of rows you want to fetch, if all is true, all rows are returned
    public static List<LeaderboardRow> getRows(int count, boolean all) throws SQLException, ClassNotFoundException {
        ResultSet rs = executeQuery(connectToDB(), "select * from "+dbName+".leaderboard;");
        List<LeaderboardRow> rows = new ArrayList<>();
        while (rs.next() && (count > 0 || all))
        {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            int waves = rs.getInt(3);
            Date date = rs.getDate(4);
            LeaderboardRow row = new LeaderboardRow(id, name, waves, date);
            rows.add(row);
            if (!all)
                count--;
        }
        return  rows;
    }

    public static void addRow(LeaderboardRow row) throws SQLException, ClassNotFoundException {
        String query;
        if (row.getID() == -1) { // no id specified so auto-inc
            String values = "('" + row.getName() + "', " + row.getWaves() + ", '" + row.getDateTime() + "')";
            query = "INSERT INTO "+dbName+".leaderboard (name, waves, date) VALUES " + values;
        }
        else {
            String values = "(" +  row.getID() + ", '" + row.getName() + "', " + row.getWaves() + ", '" + row.getDateTime() + "')";
            query = "INSERT INTO "+dbName+".leaderboard (id, name, waves, date) VALUES " + values;
        }
        Connection conn = connectToDB();
        Statement myStmt = conn.createStatement();
        myStmt.execute(query);
    }

    public static void deleteRow(int id) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM "+dbName+".leaderboard WHERE id = " + id;
        Connection conn = connectToDB();
        Statement myStmt = conn.createStatement();
        myStmt.execute(query);
    }

    private static Connection connectToDB() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(dbURL);
        return conn;
    }

    private static ResultSet executeQuery(Connection conn, String query) throws SQLException {
        return conn.prepareStatement(query).executeQuery();
    }

    private static void printOutTable() throws SQLException, ClassNotFoundException {
        List<LeaderboardRow> rows = getRows(0, true);
        for (int i = 0; i < rows.size(); i++)
        {
            System.out.println(rows.get(i).ToString());
        }
    }

    public static void main (String[] args) throws SQLException, ClassNotFoundException {
        printOutTable();
    }
}
