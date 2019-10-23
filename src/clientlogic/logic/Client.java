/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientlogic.logic;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.exception.*;
import utillities.beans.User;
import utillities.beans.Message;
import utillities.interfaces.Connectable;
/**
 *
 * @author Gaizka
 */
public class Client implements Connectable{
    private static final Logger LOGGER=Logger.getLogger("clientlogic.logic.Client");
    private final int PORT8=0;
    private final String IP="0";
    Message message;
    Socket socket;
    ObjectOutputStream send;
    ObjectInputStream receive;
    
    public Client(){
        socket = new Socket();
        try {
            send= new ObjectOutputStream(socket.getOutputStream());
            receive= new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }

    /**
     * Send a Login petition to the server
     * @param Gaizka
     */
    @Override
     public User logIn(User user) throws LoginNotFoundException,WrongPasswordException,LogicException{
        LOGGER.info("Iniciada petición Login");
       
        
        
        try {
            
            
           /**
           * Create a new message with the user who had received
           */
            message = new Message();
            message.setUser(user);
            message.setType("Login");
            send.writeObject(message);
            
            message = (Message) receive.readObject();
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
         
         return message.getUser();
         
    }
    
    /**
     * Send a SignUp petition to the server
     * @param Gaizka 
     */
    @Override
    public void signUp(User user){
        LOGGER.info("Iniciada petición de SignUp");
        socket = new Socket();
        try {
            send= new ObjectOutputStream(socket.getOutputStream());
            receive= new ObjectInputStream(socket.getInputStream());
            
            message = new Message();
            message.setUser(user);
            message.setType("SignUp");
            send.writeObject(message);
            
            message= (Message) receive.readObject();
            if(message.getType().equalsIgnoreCase("Ok")){
                
            }
            if(message.getType().equalsIgnoreCase("Error")){
                
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
    /**
     * Send a LogOut petition to the server
     * @param user 
     */
    @Override
    public void logOut(User user){
        LOGGER.info("Iniciada de petición de LogOut");
        socket = new Socket();
        try {
            send= new ObjectOutputStream(socket.getOutputStream());
            receive= new ObjectInputStream(socket.getInputStream());
            
            message = new Message();
            message.setUser(user);
            message.setType("LogOut");
            send.writeObject(message);
            
            message = (Message) receive.readObject();
            if(message.getType().equalsIgnoreCase("Ok")){
                
            }
            if(message.getType().equalsIgnoreCase("Error")){
                
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

