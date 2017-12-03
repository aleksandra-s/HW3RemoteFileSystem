/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework3.client.startup;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import se.kth.id1212.nphomework3.client.view.InputHandler;
import se.kth.id1212.nphomework3.common.ServerRemoteInterface;

/**
 *
 * @author aleks_uuia3ly
 */
public class Main {
     public static void main(String[] args) {
        try {
            ServerRemoteInterface server = (ServerRemoteInterface) Naming.lookup("filesystem");
            new InputHandler().start(server);
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            System.out.println("Could not start client.");
        }
    }
}
