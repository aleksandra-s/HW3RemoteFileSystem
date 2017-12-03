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
import java.util.logging.Level;
import java.util.logging.Logger;
import se.kth.id1212.nphomework3.server.model.ConnectedClient;
import se.kth.id1212.nphomework3.server.model.DatabaseAccess;
import se.kth.nphomework3.common.ClientRemoteInterface;
import se.kth.nphomework3.common.ServerRemoteInterface;

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
        fileDb = new DatabaseAccess(databaseName);
        idIncrementer = 0;
    }

    @Override
    public long login(String username, String password, ClientRemoteInterface remoteNode) throws RemoteException {
        try {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                idIncrementer += idIncrementer;
                long userId = idIncrementer;
                ConnectedClient newClient = new ConnectedClient(username, remoteNode, userId);
                connectedClients.put(userId, newClient);
                return userId;
            }
        } catch (SQLException ex) {
            //return 0;
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
            return true;
        } catch (SQLException ex) {
            //return registered;
            //send msg to client
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean unregister(String username, String password) throws RemoteException {
        try {
            return fileDb.unregisterUser(username, password);
        } catch (SQLException ex) {
            //Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
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
            //Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateFileName(long userID, String fileName, String newName) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateFilePath(long userID, String fileName, String filePath) throws RemoteException {
        String username = connectedClients.get(userID).getUsername();
        try {
            fileDb.updateFilePath(fileName, username, filePath); //To change body of generated methods, choose Tools | Templates.
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateFilePrivacy(long userID, String fileName, boolean privacy) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateFileReadability(long userID, String fileName, boolean readable) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateFileWriteability(long userID, String fileName, boolean writeable) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateFileNotifications(long userID, String fileName, boolean notification) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList listFiles() throws RemoteException {
        try {
            fileDb.listFiles(); //To change body of generated methods, choose Tools | Templates.
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public String readFile(long userID, String fileName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String writeFile(long userID, String fileName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
