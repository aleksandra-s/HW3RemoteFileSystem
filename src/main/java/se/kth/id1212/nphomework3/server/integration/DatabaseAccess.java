/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework3.server.integration;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author aleks_uuia3ly
 */
public class DatabaseAccess {
     //private static final String TABLE_NAME = "person";
    private static final String USER_TABLE_NAME = "users";
    private static final String FILE_TABLE_NAME = "files";
    private PreparedStatement registerUserStmt;
    private PreparedStatement unregisterUserStmt;
    private PreparedStatement uploadFileStmt;
    private PreparedStatement deleteFileStmt;
    //private PreparedStatement updateFileStmt;
   /* private PreparedStatement updateFileNameStmt; //only can be done if file owner
    private PreparedStatement updateFilePrivacyStmt; //only can be done by file owner
    private PreparedStatement updateReadPrivacyStmt; //only can be done by file owner
    private PreparedStatement updatedWritePrivacyStmt; //only can be done by file owner
    private PreparedStatement updateNotificationStmt; //only can be done by file owner*/
    private PreparedStatement listAllFilesStmt;
    private PreparedStatement listAllUsersStmt;

    private void accessDB() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientXADataSource");
            Connection connection = DriverManager.getConnection(
                    "jdbc:derby://localhost:1527/MyTestDatabase", "jdbc",
                    "jdbc");
            createUserTable(connection);
            createFileTable(connection);
            //Statement stmt = connection.createStatement();
            prepareStatements(connection);
            unregisterUserStmt.setString(1, "stina");
            unregisterUserStmt.executeUpdate();
            deleteFileStmt.setString(1, "stina's file");
            deleteFileStmt.executeUpdate();
            registerUserStmt.setString(1, "stina");
            registerUserStmt.setString(2, "abcd");
            //createPersonStmt.setInt(3, 43);
            registerUserStmt.executeUpdate();
            uploadFileStmt.setString(1, "stina's file");
            uploadFileStmt.setString(2, "stina");
            uploadFileStmt.setString(3, "file path");
            uploadFileStmt.setBoolean(4, true);
            uploadFileStmt.setBoolean(5, true);
            uploadFileStmt.setBoolean(6, false);
            uploadFileStmt.setBoolean(7, true);
            uploadFileStmt.executeUpdate();
            //createPersonStmt.executeUpdate();
            listAllFiles(connection);
            //deletePersonStmt.setString(1, "stina");
            //deletePersonStmt.executeUpdate();
            //listAllRows(connection);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void createUserTable(Connection connection) throws SQLException {
        if (!tableExists(connection, USER_TABLE_NAME)) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(
                    "create table " + USER_TABLE_NAME + " (username varchar(225) primary key, password varchar(12))");
        }
    }
    
     private void createFileTable(Connection connection) throws SQLException {
        if (!tableExists(connection, FILE_TABLE_NAME)) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(
                    "create table " + FILE_TABLE_NAME + " (filename varchar(225) primary key, username varchar(225), filepath varchar(225), privacy boolean, readable boolean, writeable boolean, notification boolean)");
        }
    }


    private boolean tableExists(Connection connection, String inputTableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tableMetaData = metaData.getTables(null, null, null, null);
        while (tableMetaData.next()) {
            String tableName = tableMetaData.getString(3);
            if (tableName.equalsIgnoreCase(inputTableName)) {
                return true;
            }
        }
        return false;
    }

    private void listAllFiles(Connection connection) throws SQLException {
        ResultSet files = listAllFilesStmt.executeQuery();
        String privacy = "";
        String read = "";
        String write = "";
        String notifications = "";
        while (files.next()) {
            if(files.getBoolean(4)){
                privacy = "public";
            }
            else{
                privacy = "private";
            }
            if(files.getBoolean(5)){
                read = "readable";
            }
            else{
                read = "unreadable";
            }
            if(files.getBoolean(6)){
                write = "writeable";
            }
            else{
                write = "unwriteable";
            }
            if(files.getBoolean(7)){
                notifications = "notifications on";
            }
            else{
                notifications = "notifications off";
            }
            System.out.println(
                    "name: " + files.getString(1) + ", owner: " + files.getString(2) + ", file path: " + files.
                    getString(3) + ", " + privacy + ", " + read + ", " + write + ", " + notifications);
        }
    }

    private void prepareStatements(Connection connection) throws SQLException {
        registerUserStmt = connection.prepareStatement("INSERT INTO " + USER_TABLE_NAME + " VALUES (?, ?)");
        unregisterUserStmt = connection.prepareStatement("DELETE FROM " + USER_TABLE_NAME + " WHERE username = ?");
        uploadFileStmt = connection.prepareStatement("INSERT INTO " + FILE_TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?)");
        deleteFileStmt = connection.prepareStatement("DELETE FROM " + FILE_TABLE_NAME + " WHERE filename = ?");
        listAllUsersStmt = connection.prepareStatement("SELECT * from " + USER_TABLE_NAME);
        listAllFilesStmt = connection.prepareStatement("SELECT * from " + FILE_TABLE_NAME);
        
    }

    public static void main(String[] args) {
        new DatabaseAccess().accessDB();
    }
}
