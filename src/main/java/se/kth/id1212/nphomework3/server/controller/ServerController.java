/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework3.server.controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import se.kth.id1212.nphomework3.server.model.ConnectedClient;
import se.kth.id1212.nphomework3.server.model.DatabaseAccess;
import se.kth.id1212.nphomework3.common.ClientRemoteInterface;
import se.kth.id1212.nphomework3.common.ServerRemoteInterface;

/**
 *
 * @author aleks_uuia3ly
 */
public class ServerController extends UnicastRemoteObject implements ServerRemoteInterface{
    private final DatabaseAccess fileDb;
    private final Map<Long, ConnectedClient> connectedClients = Collections.synchronizedMap(new HashMap<>());
    private long idIncrementer;

    public ServerController(String databaseName) throws RemoteException, ClassNotFoundException, SQLException{
        super();
        fileDb = new DatabaseAccess(databaseName, this);
        idIncrementer = 0;
    }

    @Override
    public long login(String username, String password, ClientRemoteInterface remoteNode) throws RemoteException {
        try {
            if(fileDb.checkUserLogin(username, password)){
                Iterator it = connectedClients.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry checkClient = (Map.Entry) it.next();
                    ConnectedClient c = (ConnectedClient) checkClient.getValue();
                    if(c.getUsername().equals(username)){
                        connectedClients.remove(c.getID());
                        break;
                    }
                }
                idIncrementer = ++idIncrementer;
                long userId = idIncrementer;
                ConnectedClient newClient = new ConnectedClient(username, remoteNode, userId);
                connectedClients.put(userId, newClient);
                return userId;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @Override
    public void logout(long userID) throws RemoteException {
        connectedClients.remove(userID);
    }

    @Override
    public boolean register(String username, String password) throws RemoteException {
        try {
            fileDb.registerUser(username, password);
            System.out.println("registered user");
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean unregister(String username, String password) throws RemoteException {
        try {
            //System.out.println("unregistering user " + username);
            return fileDb.unregisterUser(username, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean uploadFile(long userID, String fileName, String filePath, boolean privacy, boolean readable, boolean writeable, boolean notification) throws RemoteException {
        String username = connectedClients.get(userID).getUsername();
        try {
            fileDb.uploadFile(fileName, username, filePath, privacy, readable, writeable, notification);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't upload file");
        }
        return false;
    }

    @Override
    public boolean updateFileName(long userID, String fileName, String newName) throws RemoteException {
        String username = connectedClients.get(userID).getUsername();
        try {
            fileDb.updateFileName(fileName, username, newName); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't update file");
        }
        return false;
    }

    @Override
    public boolean updateFilePath(long userID, String fileName, String filePath) throws RemoteException {
        String username = connectedClients.get(userID).getUsername();
        try {
            fileDb.updateFilePath(fileName, username, filePath); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't update file");
        }
        return false;
    }

    @Override
    public boolean updateFilePrivacy(long userID, String fileName, boolean privacy) throws RemoteException {
        String username = connectedClients.get(userID).getUsername();
        try {
            fileDb.updateFilePrivacy(fileName, username, privacy);
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't update file");
        }
        return false;
    }

    @Override
    public boolean updateFileReadability(long userID, String fileName, boolean readable) throws RemoteException {
        String username = connectedClients.get(userID).getUsername();
        try {
            fileDb.updateFileReadability(fileName, username, readable);
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't update file");
        }
        return false;
    }

    @Override
    public boolean updateFileWriteability(long userID, String fileName, boolean writeable) throws RemoteException {
        String username = connectedClients.get(userID).getUsername();
        try {
            fileDb.updateFileWriteability(fileName, username, writeable); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't update file");
        }
        return false;
    }

    @Override
    public boolean updateFileNotifications(long userID, String fileName, boolean notification) throws RemoteException {
        String username = connectedClients.get(userID).getUsername();
        try {
            fileDb.updateFileNotification(fileName, username, notification); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't update file");
        }
        return false;
    }

    @Override
    public ArrayList listFiles(long userID) throws RemoteException {
         String username = connectedClients.get(userID).getUsername();
        try {
            return fileDb.listFiles(username); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't list files");
        }
        return null;
    }

    @Override
    public String readFile(long userID, String fileName) throws RemoteException {
        String username = connectedClients.get(userID).getUsername(); 
        try {
            return fileDb.readFile(fileName, username);
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't read file");
        }
        return null;
    }

    @Override
    public String writeFile(long userID, String fileName) throws RemoteException {
         String username = connectedClients.get(userID).getUsername(); 
        try {
            return fileDb.writeFile(fileName, username);
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't read file");
        }
        return null;
    }
    
    @Override
    public void deleteFile(long userID, String filename) throws RemoteException{
        String username = connectedClients.get(userID).getUsername(); 
        try {
            fileDb.deleteFile(filename, username);
        } catch (SQLException ex) {
            sendToClient(userID, "Couldn't delete file");;
        }
    }
    
    public void notifyClient(String username, String filename) throws RemoteException{
           Iterator it = connectedClients.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry checkClient = (Map.Entry) it.next();
                    ConnectedClient c = (ConnectedClient) checkClient.getValue();
                    if(c.getUsername().equals(username)){
                        String msg = "A user has accessed file " + filename;
                        sendToClient(c.getID(),msg);
                        break;
                    }
                }
    }
    
    private void sendToClient(long userID, String msg) throws RemoteException{
        ConnectedClient client = connectedClients.get(userID);
        client.getRemoteNode().recvMsg(msg);
    }
}
