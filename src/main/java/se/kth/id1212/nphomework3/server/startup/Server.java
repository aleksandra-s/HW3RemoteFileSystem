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
import se.kth.nphomework3.common.ClientRemoteInterface;
//import se.kth.id1212.db.bankjdbc.common.Bank;
//import se.kth.id1212.db.bankjdbc.server.controller.Controller;
//import se.kth.id1212.db.bankjdbc.server.integration.BankDBException;

/**
 * Starts the bank server.
 */
public class Server {
    private static final String USAGE = "database name";
    private String fileSystemName = "filesystem";
    private String databaseName = "test";
    //private String dbms = "derby";
    private ServerController contr;

    public static void main(String[] args) {
        try {
            long testID;
            Server server = new Server();
            server.parseCommandLineArgs(args);
            server.startRMIServant();
            System.out.println("Bank server started.");
            ServerController test = new ServerController("test2");
            test.register("stina", "abcd");
            testID = test.login("stina", "abcd", new ClientRemoteInterface() {
                @Override
                public void recvMsg(String msg) throws RemoteException {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            test.uploadFile(testID, "Stina's file", "file path", true, true, true, true);
            
            test.listFiles();
            test.updateFilePath(testID, "Stina's file", "new file path");
            test.listFiles();
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
        //ServerController contr = new ServerController(databaseName);
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
