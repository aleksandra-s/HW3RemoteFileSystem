/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework3.client.view;

/**
 *
 * @author aleks_uuia3ly
 */

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import se.kth.nphomework3.common.ClientRemoteInterface;
import se.kth.nphomework3.common.ServerRemoteInterface;
//import se.kth.id1212.nphomework3.client.controller.ClientController;

public class InputHandler implements Runnable{
    //private final ClientController contr = new ClientController();;
    private boolean receivingCmds = false;
    private final Scanner console = new Scanner(System.in);
    private final OutputHandler outputHandler = new OutputHandler();
    private final ClientRemoteInterface myRemoteObj;
    private ServerRemoteInterface server;
    private long myIdAtServer;
    Command cmd;
    
    public InputHandler() throws RemoteException{
        myRemoteObj = new ConsoleOutput();
        //this.server = server;
    }
    
    public enum Command{
        HELP,
        LOGIN,
        LOGOUT,
        REGISTER,
        UNREGISTER,
        UPLOAD_FILE,
        UPDATE_FILE_NAME,
        UPDATE_FILE_PATH,
        UPDATE_FILE_PRIVACY,
        UPDATE_FILE_READABILITY,
        UPDATE_FILE_WRITEABILITY,
        UPDATE_FILE_NOTIFICATION,
        READ_FILE,
        WRITE_FILE,
        LIST_FILE,
        UNKNOWN
    }
    
    public void start(ServerRemoteInterface server) {
        this.server = server;
        if (receivingCmds) {
            return;
        }
        receivingCmds = true;
        //contr = new ClientController();
        //outputHandler = new OutputHandler();
        new Thread(this).start();
    }

    /**
     * Interprets and performs user commands.
     */
    @Override
    public void run() {
        while (receivingCmds) {
            try {
                String clientInput = readNextLine();
                int i = clientInput.indexOf(' ');
                int j;
                String string1 = "";
                String string2 = "";
                String username;
                String password;
                //System.out.println(i);
                cmd = Command.UNKNOWN;
                if(i < 0){
                    if(clientInput.equals("help")){
                        cmd = Command.HELP;
                    }
                    else if(clientInput.equals("logout")){
                        cmd = Command.LOGOUT;
                    }
                    else if(clientInput.equals("list")){
                        cmd = Command.LIST_FILE;
                    }
                }
                else{
                    string1 = clientInput.substring(0,i);
                    if(string1.equals("login")){
                        cmd = Command.LOGIN;
                        //string2 = clientInput.substring(i + 1);
                        //int j = clientInput.indexOf(' ');
                        
                    }
                    else if(string1.equals("register")){
                        cmd = Command.REGISTER;
                        //string2 = clientInput.substring(i + 1);
                    }
                    else if(string1.equals("unregister")){
                        cmd = Command.UNREGISTER;
                        //string2 = clientInput.substring(i + 1);
                    }
                    else if(string1.equals("upload")){
                        cmd = Command.UPLOAD_FILE;
                        //string2 = clientInput.substring(i + 1);
                    }
                    else if(string1.equals("read")){
                        cmd = Command.READ_FILE;
                        
                        //string2 = clientInput.substring(i + 1);
                    }
                    else if(string1.equals("write")){
                        cmd = Command.WRITE_FILE;
                        
                        //string2 
                    }
                    else if(string1.equals("update")){
                        getUpdateCommand(clientInput.substring(i + 1));
                    }
                    string2 = clientInput.substring(i + 1);
                    //cmd = getCommand(command,rest)
                }
                switch (cmd) {
                    case HELP:
                        //receivingCmds = false;
                        /*server.leaveConversation(myIdAtServer);
                        boolean forceUnexport = false;
                        UnicastRemoteObject.unexportObject(myRemoteObj, forceUnexport);*/
                        outputHandler.printInstructions();
                        break;
                    case LOGIN:
                        /*lookupServer(cmdLine.getParameter(0));
                        myIdAtServer
                                = server.login(myRemoteObj,
                                               new Credentials(cmdLine.getParameter(1),
                                                               cmdLine.getParameter(2)));*/
                        j = string2.indexOf(' ');
                        username = string2.substring(0,j);
                        password = string2.substring(j);
                        System.out.println(username + " " + password);
                        
                        break;
                    case LOGOUT:
                        //server.changeNickname(myIdAtServer, cmdLine.getParameter(0));
                        break;
                    case REGISTER:
                        //server.changeNickname(myIdAtServer, cmdLine.getParameter(0));
                        j = string2.indexOf(' ');
                        username = string2.substring(0,j);
                        password = string2.substring(j);
                        server.register(username, password);
                        break;
                    case UNREGISTER:
                        //server.changeNickname(myIdAtServer, cmdLine.getParameter(0));
                        break;
                    case UPLOAD_FILE:
                        //server.changeNickname(myIdAtServer, cmdLine.getParameter(0));
                        break;
                    case UPDATE_FILE_NAME:
                        //server.changeNickname(myIdAtServer, cmdLine.getParameter(0));
                        break;
                    case UPDATE_FILE_PATH:
                        //server.changeNickname(myIdAtServer, cmdLine.getParameter(0));
                        break;
                    case UPDATE_FILE_PRIVACY:
                        //server.changeNickname(myIdAtServer, cmdLine.getParameter(0));
                        break;
                    case UPDATE_FILE_READABILITY:
                        //server.changeNickname(myIdAtServer, cmdLine.getParameter(0));
                        break;
                    case UPDATE_FILE_WRITEABILITY:
                        //server.changeNickname(myIdAtServer, cmdLine.getParameter(0));
                        break;
                    case UPDATE_FILE_NOTIFICATION:
                        //server.changeNickname(myIdAtServer, cmdLine.getParameter(0));
                        break;
                    case LIST_FILE:
                        break;
                    case READ_FILE:
                        //server.changeNickname(myIdAtServer, cmdLine.getParameter(0));
                        break;
                    case WRITE_FILE:
                        break;
                    case UNKNOWN:
                        break;
                    default:
                        //server.broadcastMsg(myIdAtServer, cmdLine.getUserInput());
                }
            } catch (Exception e) {
                outputHandler.printLn("Operation failed");
            }
        }
    }
    
    private String readNextLine() {
        outputHandler.print(">");
        return console.nextLine();
    }
    
    private void getUpdateCommand(String input){
        int j = input.indexOf(' ');
        String updateCommand = input.substring(0,j);
        System.out.println(updateCommand);
        if(updateCommand.equals("name")){
            cmd = Command.UPDATE_FILE_NAME;
        }
        else if(updateCommand.equals("path")){
            cmd = Command.UPDATE_FILE_PATH;
        }
        else if(updateCommand.equals("readability")){
            cmd = Command.UPDATE_FILE_READABILITY;
        }
        else if(updateCommand.equals("writeability")){
            cmd = Command.UPDATE_FILE_WRITEABILITY;
        }
        else if(updateCommand.equals("privacy")){
            cmd = Command.UPDATE_FILE_PRIVACY;
        }
        else if(updateCommand.equals("notification")){
            cmd = Command.UPDATE_FILE_NOTIFICATION;
        }
    }
    
     private class ConsoleOutput extends UnicastRemoteObject implements ClientRemoteInterface {

        public ConsoleOutput() throws RemoteException {
        }

        @Override
        public void recvMsg(String msg) {
            outputHandler.printLn((String) msg);
        }
    }
}
