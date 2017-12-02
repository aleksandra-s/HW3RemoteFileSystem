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
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.kth.id1212.nphomework3.server.controller.ServerController;
//import se.kth.id1212.db.bankjdbc.common.Bank;
//import se.kth.id1212.db.bankjdbc.server.controller.Controller;
//import se.kth.id1212.db.bankjdbc.server.integration.BankDBException;

/**
 * Starts the bank server.
 */
public class Server {
    private static final String USAGE = "java bankjdbc.Server [bank name in rmi registry] "
                                        + "[bank database name] [dbms: derby or mysql]";
    private String fileSystemName = "filesystem";
    private String databaseName = "test";
    //private String dbms = "derby";

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.parseCommandLineArgs(args);
            server.startRMIServant();
            System.out.println("Bank server started.");
        } catch (RemoteException | MalformedURLException | ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed to start bank server.");
        }
    }

    private void startRMIServant() throws RemoteException, MalformedURLException, ClassNotFoundException, SQLException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException noRegistryRunning) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
        ServerController contr = new ServerController(databaseName);
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
