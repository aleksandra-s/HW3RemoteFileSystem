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
import java.util.ArrayList;
import java.util.Scanner;
import se.kth.nphomework3.common.ClientRemoteInterface;
import se.kth.nphomework3.common.ServerRemoteInterface;
//import se.kth.id1212.nphomework3.client.controller.ClientController;

public class InputHandler implements Runnable{
    private boolean receivingCmds = false;
    private final Scanner console = new Scanner(System.in);
    private final OutputHandler outputHandler = new OutputHandler();
    private final ClientRemoteInterface myRemoteObj;
    private ServerRemoteInterface server;
    private long myIdAtServer;
    Command cmd;
    boolean loggedIn = false;
    
    public InputHandler() throws RemoteException{
        myRemoteObj = new ConsoleOutput();
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
        DELETE_FILE,
        UNKNOWN
    }
    
    public void start(ServerRemoteInterface server) {
        this.server = server;
        if (receivingCmds) {
            return;
        }
        receivingCmds = true;
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
                int k;
                String string1 = "";
                String string2 = "";
                String username;
                String password;
                String fileName;
                String filePath;
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
                    }
                    else if(string1.equals("register")){
                        cmd = Command.REGISTER;
                    }
                    else if(string1.equals("unregister")){
                        cmd = Command.UNREGISTER;
                    }
                    else if(string1.equals("upload")){
                        cmd = Command.UPLOAD_FILE;
                    }
                    else if(string1.equals("read")){
                        cmd = Command.READ_FILE;
                    }
                    else if(string1.equals("write")){
                        cmd = Command.WRITE_FILE;
                    }
                    else if(string1.equals("delete")){
                        cmd = Command.DELETE_FILE;
                    }
                    else if(string1.equals("update")){
                        getUpdateCommand(clientInput.substring(i + 1));
                    }
                    string2 = clientInput.substring(i + 1);
                }
                switch (cmd) {
                    case HELP:
                        outputHandler.printInstructions();
                        break;
                    case LOGIN:
                        outputHandler.printLn("Logging in");
                        j = string2.indexOf(' ');
                        username = string2.substring(0,j);
                        password = string2.substring(j);
                        System.out.println(username + " " + password);
                        myIdAtServer = server.login(username, password, myRemoteObj);
                        if(myIdAtServer != 0){
                            loggedIn = true;
                        }
                        break;
                    case LOGOUT:
                        if(loggedIn){
                            outputHandler.printLn("Logging out");
                            receivingCmds = false;
                            server.logout(myIdAtServer);
                            boolean forceUnexport = false;
                            UnicastRemoteObject.unexportObject(myRemoteObj, forceUnexport);
                        }
                        else{
                            outputHandler.printLn("Not logged in");
                        }
                        break;
                    case REGISTER:
                        outputHandler.printLn("Registering user");
                        j = string2.indexOf(' ');
                        username = string2.substring(0,j);
                        password = string2.substring(j);
                        server.register(username, password);
                        break;
                    case UNREGISTER:
                        outputHandler.printLn("Unregistering user");
                        j = string2.indexOf(' ');
                        username = string2.substring(0,j);
                        password = string2.substring(j);
                        server.unregister(username, password);
                        break;
                    case UPLOAD_FILE:
                        if(loggedIn){
                            String privacyAnswer;
                            String notificationAnswer;
                            boolean privacy = false;
                            boolean readable = false;
                            boolean writeable = false;
                            boolean notification = false;
                            j = string2.indexOf(' ');
                            fileName = string2.substring(0,j);
                            filePath = string2.substring(j);
                            outputHandler.askPrivacy();
                            privacyAnswer = readNextLine();
                            if(privacyAnswer.equals("yes")){
                                privacy = true; //public
                                String readableAnswer;
                                String writeableAnswer;
                                outputHandler.askReadability();
                                readableAnswer = readNextLine();
                                if(readableAnswer.equals("yes")){
                                    readable = true;
                                }
                                else if(!readableAnswer.equals("no")){
                                outputHandler.printLn("Invalid answer");
                                break;
                                }
                                outputHandler.askWriteability();
                                writeableAnswer = readNextLine();
                                if(writeableAnswer.equals("yes")){
                                    writeable = true;
                                }
                                else if(!writeableAnswer.equals("no")){
                                outputHandler.printLn("Invalid answer");
                                break;
                                }
                            }
                            else if(!privacyAnswer.equals("no")){
                                outputHandler.printLn("Invalid answer");
                                break;
                            }
                            outputHandler.askNotifications();
                            notificationAnswer = readNextLine();
                        if(notificationAnswer.equals("yes")){
                            notification = true;
                        }
                        else if(!notificationAnswer.equals("no")){
                            outputHandler.printLn("Invalid answer");
                            break;
                        }
                        outputHandler.printLn("uploading file");
                        server.uploadFile(myIdAtServer, fileName, filePath, privacy, readable, writeable, notification);}
                        else{
                            outputHandler.printLn("Not logged in");
                        }
                        break;
                    case UPDATE_FILE_NAME:
                        if(loggedIn){
                            outputHandler.printLn("updating file name");
                            j = string2.indexOf(' ');
                            String string3 = string2.substring(j + 1);
                            k = string3.indexOf(' ');
                            fileName = string3.substring(0,k);
                            String newName = string3.substring(k + 1);
                            server.updateFileName(myIdAtServer, fileName, newName);
                        }
                        else{
                            outputHandler.printLn("Not logged in");
                        }
                        break;
                    case UPDATE_FILE_PATH:
                        if(loggedIn){
                            outputHandler.printLn("updating file path");
                            j = string2.indexOf(' ');
                            String string3 = string2.substring(j + 1);
                            k = string3.indexOf(' ');
                            fileName = string3.substring(0,k);
                            String newPath = string3.substring(k + 1);
                            server.updateFilePath(myIdAtServer, fileName, newPath);
                        }
                        else{
                            outputHandler.printLn("Not logged in");
                        }
                        break;
                    case UPDATE_FILE_PRIVACY:
                        if(loggedIn){
                            outputHandler.printLn("updating file privacy");
                            boolean privacy = false;
                            j = string2.indexOf(' ');
                            String string3 = string2.substring(j + 1);
                            k = string3.indexOf(' ');
                            fileName = string3.substring(0,k);
                            String newPrivacy = string3.substring(k + 1);
                            if(newPrivacy.equals("public")){
                                privacy = true;
                            }
                            else if(!newPrivacy.equals("private")){
                                outputHandler.printLn("Invalid command");
                                break;
                            }
                            server.updateFilePrivacy(myIdAtServer, fileName, privacy);
                        }
                        else{
                            outputHandler.printLn("Not logged in");
                        }
                        break;
                    case UPDATE_FILE_READABILITY:
                        if(loggedIn){
                            outputHandler.printLn("updating file readability");
                            boolean readable = false;
                            j = string2.indexOf(' ');
                            String string3 = string2.substring(j + 1);
                            k = string3.indexOf(' ');
                            fileName = string3.substring(0,k);
                            String newReadable = string3.substring(k + 1);
                            if(newReadable.equals("readable")){
                                readable = true;
                            }
                            else if(!newReadable.equals("unreadable")){
                                outputHandler.printLn("Invalid command");
                                break;
                            }
                            server.updateFileReadability(myIdAtServer, fileName, readable);
                        }
                        else{
                            outputHandler.printLn("Not logged in");
                        }
                        break;
                    case UPDATE_FILE_WRITEABILITY:
                        if(loggedIn){
                            outputHandler.printLn("updating file writeability");
                            boolean writeable = false;
                            j = string2.indexOf(' ');
                            String string3 = string2.substring(j + 1);
                            k = string3.indexOf(' ');
                            fileName = string3.substring(0,k);
                            String newWriteable = string3.substring(k + 1);
                            if(newWriteable.equals("writeable")){
                                writeable = true;
                            }
                            else if(!newWriteable.equals("unwriteable")){
                                outputHandler.printLn("Invalid command");
                                break;
                            }
                            server.updateFileWriteability(myIdAtServer, fileName, writeable);
                        }
                        else{
                            outputHandler.printLn("Not logged in");
                        }
                        break;
                    case UPDATE_FILE_NOTIFICATION:
                        //server.changeNickname(myIdAtServer, cmdLine.getParameter(0));
                        if(loggedIn){
                            outputHandler.printLn("updating file notification");
                            boolean notification = false;
                            j = string2.indexOf(' ');
                            String string3 = string2.substring(j + 1);
                            k = string3.indexOf(' ');
                            fileName = string3.substring(0,k);
                            String newNotification = string3.substring(k + 1);
                            if(newNotification.equals("notify")){
                                notification = true;
                            }
                            else if(!newNotification.equals("unnotify")){
                                outputHandler.printLn("Invalid command");
                                break;
                            }
                            server.updateFileNotifications(myIdAtServer, fileName, notification);
                        }
                        else{
                            outputHandler.printLn("Not logged in");
                        }
                        break;
                    case LIST_FILE:
                        if(loggedIn){
                            ArrayList<String> list = server.listFiles(myIdAtServer);
                            outputHandler.printList(list);
                        }
                        else{
                            outputHandler.printLn("Not logged in");
                        }
                        break;
                    case READ_FILE:
                        if(loggedIn){
                            fileName = string2;
                            outputHandler.printLn("File path to read: " + server.readFile(myIdAtServer,fileName));
                        }
                        else{
                            outputHandler.printLn("Not logged in");
                        }
                        break;
                    case WRITE_FILE:
                        if(loggedIn){
                            fileName = string2;
                            outputHandler.printLn("File path to write: " + server.writeFile(myIdAtServer,fileName));
                        }
                        else{
                            outputHandler.printLn("Not logged in");
                        }
                        break;
                    case DELETE_FILE:
                        if(loggedIn){
                            outputHandler.printLn("deleting file");
                            fileName = string2;
                            server.deleteFile(myIdAtServer,fileName);
                        }
                        else{
                            outputHandler.printLn("Not logged in");
                        }
                        break;
                    case UNKNOWN:
                        break;
                }
            } catch (Exception e) {
                outputHandler.printLn("Operation failed");
                e.printStackTrace();
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
