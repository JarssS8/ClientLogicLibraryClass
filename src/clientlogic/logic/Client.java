/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientlogic.logic;
import utilities.exception.LoginNotFoundException;
import utilities.exception.WrongPasswordException;
import utilities.exception.LogicException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.beans.User;
import utilities.beans.Message;
import utilities.exception.LoginAlreadyTakenException;
import utilities.interfaces.Connectable;
/**
 * @author Gaizka Andrés
 */
public class Client implements Connectable{
    private static final Logger LOGGER=Logger.getLogger("clientlogic.logic.Client");
    private final int PORT=0;
    private final String IP="0";
    Message message;
    Socket socket;
    ObjectOutputStream send;
    ObjectInputStream receive;
    
    public Client() {
        
        try {
            socket = new Socket(IP,PORT);
            send= new ObjectOutputStream(socket.getOutputStream());
            receive= new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }

    /**
     * Send a Login petition to the server
     * @param Gaizka Andrés
     */
    @Override
     public User logIn(User user) throws LoginNotFoundException,WrongPasswordException,LogicException{
        LOGGER.info("Login petition initialize");
        try {
     
           //Create a new message with the user who had received 
            message = new Message();
            message.setUser(user);
            message.setType("Login");
            //Send the message through the socket to the server
            send.writeObject(message);
            LOGGER.info("Sending message...");
            //Receive the response of the server through the socket
            message = (Message) receive.readObject();
            LOGGER.info("Reading message...");
            //Control of error of Login
            switch(message.getType()){
                case "PasswordError":
                    throw new WrongPasswordException();
                    
                case "LoginError":
                    throw new LoginNotFoundException();         
                default:
                    throw new LogicException();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Closing the socket
        try {
            if(send!=null){
                send.close();
            }
            if(receive!=null){
                receive.close();
            }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
         // Return of the message to the controller
         LOGGER.info("Returning the message");
         return message.getUser();
         
    }
    
    /**
     * Send a SignUp petition to the server
     * @param Gaizka Andrés
     */
    @Override
    public User signUp(User user) throws LoginAlreadyTakenException,LogicException{
        LOGGER.info("SignUp petition initialize");
        try {
            //Create a new message with the user who had received 
            message = new Message();
            message.setUser(user);
            message.setType("SignUp");
            //Send the message through the socket to the server
            send.writeObject(message);
            LOGGER.info("Sending message...");
            
            //Receive the response of the server through the socket
            message= (Message) receive.readObject();
            LOGGER.info("Reading message...");
            //Control of error of Sign Up
            switch(message.getType()){
                case "AlreadyTaken":
                    throw new LoginAlreadyTakenException();
                default: 
                    throw new LogicException();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
         try {
            if(send!=null){
                send.close();
            }
            if(receive!=null){
                receive.close();
            }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
         // Return of the message to the controller
         LOGGER.info("Returning the message");
         return message.getUser();
    }
    /**
     * Send a LogOut petition to the server
     * @param user 
     */
    @Override
    public void logOut(User user) throws LogicException{
        LOGGER.info("LogOut petition initialize");
        try {
            //Create a new message with the user who had received 
            message = new Message();
            message.setUser(user);
            message.setType("LogOut");
            //Send the message through the socket to the server
            send.writeObject(message);
            LOGGER.info("Reading message...");
            
            //Receive the response of the server through the socket
            message = (Message) receive.readObject();
            LOGGER.info("Reading message...");
            //Control of error of Sign Up
            if(message.getType().equalsIgnoreCase("Error")){
                throw new LogicException();            
            }
           
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
          try {
            if(send!=null){
                send.close();
            }
            if(receive!=null){
                receive.close();
            }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

