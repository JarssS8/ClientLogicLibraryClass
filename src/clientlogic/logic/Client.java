/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientlogic.logic;
import utilities.exception.LoginNotFoundException;
import utilities.exception.WrongPasswordException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.beans.User;
import utilities.beans.Message;
import utilities.exception.LoginAlreadyTakenException;
import utilities.exception.ServerConnectionErrorException;
import utilities.interfaces.Connectable;
/**
 * @author Gaizka Andrés
 */
public class Client implements Connectable{
    private static final Logger LOGGER=Logger.getLogger("clientlogic.logic.Client");
    private final int PORT=5000;
    private final String IP="localhost";
    Message message;
    Socket socket;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    
    public Client() {
        
        try {
            socket = new Socket(IP,PORT);
            objectOutputStream= new ObjectOutputStream(socket.getOutputStream());
            objectInputStream= new ObjectInputStream(socket.getInputStream());
        } catch (Exception ex) {
            LOGGER.warning("Error con los stream");
        }
        
    }

    /**
     * Send a Login petition to the server
     * @param user
     * @author Gaizka Andrés
     * @return 
     * @throws utilities.exception.LoginNotFoundException
     * @throws utilities.exception.WrongPasswordException
     */
    @Override
     public User logIn(User user) throws LoginNotFoundException, WrongPasswordException, ServerConnectionErrorException{
        LOGGER.info("Login petition initialize");
        try {
     
           //Create a new message with the user who had received 
            message = new Message();
            message.setUser(user);
            message.setType("Login");
            //Send the message through the socket to the server
            objectOutputStream.writeObject(message);
            LOGGER.info("Sending message...");
            //Receive the response of the server through the socket
            message = (Message) objectInputStream.readObject();
            LOGGER.info("Reading message...");
            //Control of error of Login
            switch(message.getType()){
                case "LoginError":
                    throw new LoginNotFoundException(); 
                case "PasswordError":
                    throw new WrongPasswordException();         
                case "ServerError": 
                    throw new ServerConnectionErrorException();
            }
        } catch (Exception e) {
            LOGGER.warning("Error de conexión al servidor"+e.getMessage());
        }
        //Closing the socket
        try {
            if(objectOutputStream!=null){
                objectOutputStream.close();
            }
            if(objectInputStream!=null){
                objectInputStream.close();
            }
            } catch (Exception ex) {
                LOGGER.warning("Error de conexión al servidor"+ex.getMessage());
            }
         // Return of the message to the controller
         LOGGER.info("Returning the message");
         return message.getUser();
         
    }
    
    /**
     * Send a SignUp petition to the server
     * @param user
     * @author Gaizka Andrés
     * @return 
     * @throws utilities.exception.LoginAlreadyTakenException
     */
    @Override
    public User signUp(User user) throws LoginAlreadyTakenException, ServerConnectionErrorException {
        LOGGER.info("SignUp petition initialize");
        try {
            //Create a new message with the user who had received 
            message = new Message();
            message.setUser(user);
            message.setType("SignUp");
            //Send the message through the socket to the server
            objectOutputStream.writeObject(message);
            LOGGER.info("Sending message...");
            
            //Receive the response of the server through the socket
            message= (Message) objectInputStream.readObject();
            LOGGER.info("Reading message...");
            //Control of error of Sign Up
            switch(message.getType()){
                case "LoginTaken":
                    throw new LoginAlreadyTakenException();
                case "ServerError": 
                    throw new ServerConnectionErrorException();
            }
        } catch (Exception e) {
            LOGGER.warning("Server connection error"+e.getMessage());
        }
         try {
            if(objectOutputStream!=null){
                objectOutputStream.close();
            }
            if(objectInputStream!=null){
                objectInputStream.close();
            }
            } catch (IOException ex) {
                LOGGER.warning("Error closing streams"+ex.getMessage());
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
    public void logOut(User user) throws ServerConnectionErrorException{
        LOGGER.info("LogOut petition initialize");
        try {
            //Create a new message with the user who had received 
            message = new Message();
            message.setUser(user);
            message.setType("LogOut");
            //Send the message through the socket to the server
            objectOutputStream.writeObject(message);
            LOGGER.info("Reading message...");
            
            //Receive the response of the server through the socket
            message = (Message) objectInputStream.readObject();
            LOGGER.info("Reading message...");
            //Control of error of Sign Up
            if(message.getType().equalsIgnoreCase("ServerError")){
                throw new ServerConnectionErrorException();            
            }
        } catch (Exception e) {
            LOGGER.warning("Error de conexión al servidor"+e.getMessage());
        }
          try {
            if(objectOutputStream!=null){
                objectOutputStream.close();
            }
            if(objectInputStream!=null){
                objectInputStream.close();
            }
            } catch (Exception e) {
                LOGGER.warning("Error de conexión al servidor"+e.getMessage());
            }
        }
        
    }

