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
public class OutputHandler {
    public void printWord(String word){
        System.out.println(word);
    }
    
    public void printAttempts(String attempts){
        System.out.println("Failed attempts left: " + attempts);
    }
    
    public void printScore(String score){
        System.out.println("Score: " + score);
    }
    
    public void printLoss(){
        System.out.println("You lost :(");
    }
    
    public void printWin(){
        System.out.println("You won :)");
    }
    
    public void printLn(String toPrint){
        System.out.println(toPrint);
    }
    
    public void print(String toPrint){
        System.out.print(toPrint);
    }
    
    public void printInstructions(){
        System.out.println("General available commands:");
        System.out.println("login <username> <password>");
        System.out.println("logout");
        System.out.println("register <username> <password>");
        System.out.println("unregister <username> <password>");
        System.out.println("upload <file name> <file path>");
        System.out.println("read <file name>");
        System.out.println("write <file name>");
        System.out.println("list");
        System.out.println("Commands for file owners:");
        System.out.println("update name <file name> <new file name>");
        System.out.println("update path <file name> <new file path>");
        System.out.println("update privacy <file name> <public or private>");
        System.out.println("update readability <file name> <readable or unreadable>");
        System.out.println("update writeability <file name> <writeable or unwriteable>");
        System.out.println("update notification <file name> <notify or unnotify>");
    }
    
    public void askPrivacy(){
        System.out.println("Is the file public?");
    }
    
    public void askReadability(){
        System.out.println("Is the file readable?");
    }
    
    public void askWriteability(){
        System.out.println("Is the file writeable?");
    }
    
    public void askNotifications(){
        System.out.println("Do you want to be notified if someone reads or writes to the file?");
    }
}
