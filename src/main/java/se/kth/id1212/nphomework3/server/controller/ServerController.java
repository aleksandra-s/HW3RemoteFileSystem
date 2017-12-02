/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework3.server.controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import se.kth.id1212.nphomework3.server.model.DatabaseAccess;

/**
 *
 * @author aleks_uuia3ly
 */
public class ServerController extends UnicastRemoteObject {
    private final DatabaseAccess fileDb;

    public ServerController(String databaseName) throws RemoteException, ClassNotFoundException, SQLException{
        super();
        fileDb = new DatabaseAccess(databaseName);
    }
}
