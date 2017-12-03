/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework3.server.startup;

/**
 *
 * @author aleks_uuia3ly
 */
import java.net.MalformedURLException;
import java.rmi.Naming;
//import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import se.kth.id1212.nphomework3.server.controller.ServerController;

/**
 * Starts the server.
 */
public class Server {
    private static final String USAGE = "database name";
    private String fileSystemName = "filesystem";
    private String databaseName = "test";
    private ServerController contr;

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.parseCommandLineArgs(args);
            server.startRMIServant();
            System.out.println("Server started.");
        } catch (RemoteException | MalformedURLException | ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed to start server.");
        }
    }

    private void startRMIServant() throws RemoteException, MalformedURLException, ClassNotFoundException, SQLException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException noRegistryRunning) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
        contr = new ServerController(databaseName);
        Naming.rebind(fileSystemName, contr);
    }

    private void parseCommandLineArgs(String[] args) {
        if (args.length > 3 || (args.length > 0 && args[0].equalsIgnoreCase("-h"))) {
            System.out.println(USAGE);
            System.exit(1);
        }

        if (args.length > 0) {
            databaseName = args[0];
        }
    }
}
