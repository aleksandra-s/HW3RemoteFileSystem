/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework3.server.model;

import se.kth.nphomework3.common.ClientRemoteInterface;

/**
 *
 * @author aleks_uuia3ly
 */
public class ConnectedClient{
    private final long id;
    private final String username;
    private final ClientRemoteInterface remoteNode;

    public ConnectedClient(String username, ClientRemoteInterface remoteNode, long userId) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.id = userId;
        this.remoteNode = remoteNode;
        this.username = username;
    }
    
    public long getID(){
        return id;
    }
    
    public String getUsername(){
        return username;
    }
    
    public ClientRemoteInterface getRemoteNode(){
        return remoteNode;
    }
}
