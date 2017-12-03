/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework3.server.model;

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
    private PreparedStatement findUserStmt;
    private PreparedStatement findFileStmt;
    private PreparedStatement updateFileNameStmt;
    private PreparedStatement updateFilePathStmt;
    private PreparedStatement updateFilePrivacyStmt;
    private PreparedStatement updateFileReadabilityStmt;
    private PreparedStatement updateFileWriteabilityStmt;
    private PreparedStatement updateFileNotificationsStmt;
    //private PreparedStatement updateFileStmt;
   /* private PreparedStatement updateFileNameStmt; //only can be done if file owner
    private PreparedStatement updateFilePrivacyStmt; //only can be done by file owner
    private PreparedStatement updateReadPrivacyStmt; //only can be done by file owner
    private PreparedStatement updatedWritePrivacyStmt; //only can be done by file owner
    private PreparedStatement updateNotificationStmt; //only can be done by file owner*/
    private PreparedStatement listAllFilesStmt;
    private PreparedStatement listAllUsersStmt;
    private Connection connection;
    
    public DatabaseAccess(String databaseName) throws ClassNotFoundException, SQLException{
            connection = createDatasource(databaseName);
            prepareStatements(connection);
            //System.out.println("Help");
       
    }

    private void accessDB() {
        try {
            //Class.forName("org.apache.derby.jdbc.ClientXADataSource");
            /*Connection connection = DriverManager.getConnection(
                    "jdbc:derby://localhost:1527/MyTestDatabase", "jdbc",
                    "jdbc");*/
            /*Connection connection = connectToDB("test");
            createUserTable(connection);
            createFileTable(connection);*/
            //Connection connection = createDatasource("Bank");
            //Statement stmt = connection.createStatement();
            //prepareStatements(connection);
            
            registerUserStmt.setString(1, "stina");
            registerUserStmt.setString(2, "abcd");
             registerUserStmt.executeUpdate();
            registerUserStmt.setString(1, "olle");
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
            uploadFileStmt.setString(1, "stina's second file");
            uploadFileStmt.setString(2, "stina");
            uploadFileStmt.setString(3, "file path");
            uploadFileStmt.setBoolean(4, true);
            uploadFileStmt.setBoolean(5, true);
            uploadFileStmt.setBoolean(6, false);
            uploadFileStmt.setBoolean(7, true);
            uploadFileStmt.executeUpdate();
            uploadFileStmt.setString(1, "olle's file");
            uploadFileStmt.setString(2, "olle");
            uploadFileStmt.setString(3, "file path");
            uploadFileStmt.setBoolean(4, true);
            uploadFileStmt.setBoolean(5, true);
            uploadFileStmt.setBoolean(6, false);
            uploadFileStmt.setBoolean(7, true);
            uploadFileStmt.executeUpdate();
            //createPersonStmt.executeUpdate();
            listAllFiles(connection);
            updateFileReadability("stina's second file", "stina", false);
            listAllFiles(connection);
            //deletePersonStmt.setString(1, "stina");
            //deletePersonStmt.executeUpdate();
            //listAllRows(connection);
        } catch (SQLException ex1) {
            /*try {
                //ex.printStackTrace();
                unregisterUserStmt.setString(1, "olle");
                unregisterUserStmt.executeUpdate();
                deleteFileStmt.setString(1, "olle's file");
                deleteFileStmt.executeUpdate();
                unregisterUser("stina", "abcd");
                //accessDB();
            } catch (SQLException ex) {*/
                ex1.printStackTrace();
            //}
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
    
    private void prepareStatements(Connection connection) throws SQLException {
        registerUserStmt = connection.prepareStatement("INSERT INTO " + USER_TABLE_NAME + " VALUES (?, ?)");
        unregisterUserStmt = connection.prepareStatement("DELETE FROM " + USER_TABLE_NAME + " WHERE username = ?");
        uploadFileStmt = connection.prepareStatement("INSERT INTO " + FILE_TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?)");
        deleteFileStmt = connection.prepareStatement("DELETE FROM " + FILE_TABLE_NAME + " WHERE filename = ?");
        listAllUsersStmt = connection.prepareStatement("SELECT * from " + USER_TABLE_NAME);
        listAllFilesStmt = connection.prepareStatement("SELECT * from " + FILE_TABLE_NAME);
        findUserStmt = connection.prepareStatement("SELECT * from "
                                                      + USER_TABLE_NAME + " WHERE username = ?");
        findFileStmt = connection.prepareStatement("SELECT * from "
                                                      + FILE_TABLE_NAME + " WHERE filename = ?");
        updateFileNameStmt = connection.prepareStatement("UPDATE "
                                                        + FILE_TABLE_NAME
                                                        + " SET filename = ? WHERE filename = ? ");
        updateFilePathStmt = connection.prepareStatement("UPDATE "
                                                        + FILE_TABLE_NAME
                                                        + " SET filepath = ? WHERE filename = ? ");
        updateFilePrivacyStmt = connection.prepareStatement("UPDATE "
                                                        + FILE_TABLE_NAME
                                                        + " SET privacy = ? WHERE filename = ? ");
        updateFileReadabilityStmt = connection.prepareStatement("UPDATE "
                                                        + FILE_TABLE_NAME
                                                        + " SET readable = ? WHERE filename = ? ");
        updateFileWriteabilityStmt = connection.prepareStatement("UPDATE "
                                                        + FILE_TABLE_NAME
                                                        + " SET writeable = ? WHERE filename = ? ");
        updateFileNotificationsStmt = connection.prepareStatement("UPDATE "
                                                        + FILE_TABLE_NAME
                                                        + " SET notification = ? WHERE filename = ? ");
        
    }

    //METHODS WHICH CAN BE CALLED BY SERVER 
    
    public void registerUser(String username, String password) throws SQLException{ //If duplicate user should throw exception 
        registerUserStmt.setString(1, username);
        registerUserStmt.setString(2, password);
        registerUserStmt.executeUpdate();
    }
    
    public boolean unregisterUser(String username, String password) throws SQLException{
        ResultSet result = null;
        boolean deleteFiles = false;
        boolean completelyUnregistered = false;
        findUserStmt.setString(1, username);
        result = findUserStmt.executeQuery();
        if(result.next()){
            if(result.getString(2).equals(password)) {
                unregisterUserStmt.setString(1, username);
                unregisterUserStmt.executeUpdate();
                deleteFiles = true;
            }
        }
        if(deleteFiles){
            ResultSet files = listAllFilesStmt.executeQuery();
            while(files.next()){
                if(files.getString(2).equals(username)){
                    deleteFileStmt.setString(1, files.getString(1));
                    deleteFileStmt.executeUpdate();
                }
            }
            completelyUnregistered = true;
        }
        return completelyUnregistered;
    }
    
    public boolean checkUserLogin(String username, String password)throws SQLException{
        ResultSet result = null;
        boolean loggedIn = false;
        findUserStmt.setString(1, username);
        result = findUserStmt.executeQuery();
        if(result.next()){
            if(result.getString(2).equals(password)) {
                loggedIn = true;
            }
        }
        return loggedIn;
    }
    
    public void uploadFile(String filename, String username, String filepath, boolean privacy, boolean readable, boolean writeable, boolean notifications) throws SQLException{
        uploadFileStmt.setString(1, filename);
        uploadFileStmt.setString(2, username);
        uploadFileStmt.setString(3, filepath);
        uploadFileStmt.setBoolean(4, privacy);
        uploadFileStmt.setBoolean(5, readable);
        uploadFileStmt.setBoolean(6, writeable);
        uploadFileStmt.setBoolean(7, notifications);
        uploadFileStmt.executeUpdate();
    }
    
    public boolean deleteFile(String filename, String username) throws SQLException{
        ResultSet result = null;
        boolean userCorrect = false;
        boolean fileDeleted = false;
        findFileStmt.setString(1, filename);
        result = findFileStmt.executeQuery();
        if(result.next()){
            if(result.getString(2).equals(username)){
                userCorrect = true;
            }
        }
        if(userCorrect){
            deleteFileStmt.setString(1, filename);
            deleteFileStmt.executeUpdate();
            fileDeleted = true;
        }
        return fileDeleted;
    }
    
    public boolean updateFileName(String filename, String username, String newName) throws SQLException{
        boolean fileUpdated = false;
        ResultSet result = null;
        boolean userCorrect = false;
        findFileStmt.setString(1, filename);
        result = findFileStmt.executeQuery();
        if(result.next()){
            if(result.getString(2).equals(username)){
                userCorrect = true;
            }
        }
        if(userCorrect){
            updateFileNameStmt.setString(2,filename);
            updateFileNameStmt.setString(1, newName);
            updateFileNameStmt.executeUpdate();
            fileUpdated = true;
        }
        return fileUpdated;
    }
    
     public boolean updateFilePath(String filename, String username, String newPath) throws SQLException{
        boolean fileUpdated = false;
        ResultSet result = null;
        boolean userCorrect = false;
        findFileStmt.setString(1, filename);
        result = findFileStmt.executeQuery();
        if(result.next()){
            if(result.getString(2).equals(username)){
                userCorrect = true;
            }
        }
        if(userCorrect){
            updateFilePathStmt.setString(2,filename);
            updateFilePathStmt.setString(1, newPath);
            updateFilePathStmt.executeUpdate();
            fileUpdated = true;
        }
        return fileUpdated;
    }
     
    public boolean updateFilePrivacy(String filename, String username, boolean privacy) throws SQLException{
        boolean fileUpdated = false;
        ResultSet result = null;
        boolean userCorrect = false;
        findFileStmt.setString(1, filename);
        result = findFileStmt.executeQuery();
        if(result.next()){
            if(result.getString(2).equals(username)){
                userCorrect = true;
            }
        }
        if(userCorrect){
            updateFilePrivacyStmt.setString(2,filename);
            updateFilePrivacyStmt.setBoolean(1,privacy);
            updateFilePrivacyStmt.executeUpdate();
            fileUpdated = true;
        }
        return fileUpdated;
    }
    
    public boolean updateFileReadability(String filename, String username, boolean readable) throws SQLException{
        boolean fileUpdated = false;
        ResultSet result = null;
        boolean userCorrect = false;
        findFileStmt.setString(1, filename);
        result = findFileStmt.executeQuery();
        if(result.next()){
            if(result.getString(2).equals(username)){
                userCorrect = true;
            }
        }
        if(userCorrect){
            updateFileReadabilityStmt.setString(2,filename);
            updateFileReadabilityStmt.setBoolean(1,readable);
            updateFileReadabilityStmt.executeUpdate();
            fileUpdated = true;
        }
        return fileUpdated;
    }
    
    public boolean updateFileWriteability(String filename, String username, boolean writeable) throws SQLException{
        boolean fileUpdated = false;
        ResultSet result = null;
        boolean userCorrect = false;
        findFileStmt.setString(1, filename);
        result = findFileStmt.executeQuery();
        if(result.next()){
            if(result.getString(2).equals(username)){
                userCorrect = true;
            }
        }
        if(userCorrect){
            updateFileWriteabilityStmt.setString(2,filename);
            updateFileWriteabilityStmt.setBoolean(1,writeable);
            updateFileWriteabilityStmt.executeUpdate();
            fileUpdated = true;
        }
        return fileUpdated;
    }
    
    public boolean updateFileNotification(String filename, String username, boolean notification) throws SQLException{
        boolean fileUpdated = false;
        ResultSet result = null;
        boolean userCorrect = false;
        findFileStmt.setString(1, filename);
        result = findFileStmt.executeQuery();
        if(result.next()){
            if(result.getString(2).equals(username)){
                userCorrect = true;
            }
        }
        if(userCorrect){
            updateFileNotificationsStmt.setString(2,filename);
            updateFileNotificationsStmt.setBoolean(1,notification);
            updateFileNotificationsStmt.executeUpdate();
            fileUpdated = true;
        }
        return fileUpdated;
    }
    
    public String readFile(String filename, String username) throws SQLException{
        //boolean fileUpdated = false;
        ResultSet result = null;
        //boolean userCorrect = false;
        findFileStmt.setString(1, filename);
        result = findFileStmt.executeQuery();
        if(result.next()){
            if(result.getString(2).equals(username)){
                return result.getString(3);
            }
            else if(result.getBoolean(4) && result.getBoolean(5)){
                return result.getString(3);
            }
        }
        return null;
    }
    
      public String writeFile(String filename, String username) throws SQLException{
        //boolean fileUpdated = false;
        ResultSet result = null;
        //boolean userCorrect = false;
        findFileStmt.setString(1, filename);
        result = findFileStmt.executeQuery();
        if(result.next()){
            if(result.getString(2).equals(username)){
                return result.getString(3);
            }
            else if(result.getBoolean(4) && result.getBoolean(6)){
                return result.getString(3);
            }
        }
        return null;
    }
    
    public void listFiles() throws SQLException{
        listAllFiles(this.connection);
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
    
    private Connection connectToDB(String databaseName)throws ClassNotFoundException, SQLException{
        Class.forName("org.apache.derby.jdbc.ClientXADataSource");
        return DriverManager.getConnection( "jdbc:derby://localhost:1527/" + databaseName + ";create=true");
    }
    
    private Connection createDatasource(String databaseName) throws ClassNotFoundException, SQLException{
        Connection connection = connectToDB(databaseName);
        createUserTable(connection);
        createFileTable(connection);
        return connection;
    }

   /* public static void main(String[] args) throws ClassNotFoundException {
        //new DatabaseAccess("test").accessDB();
    }*/
}
