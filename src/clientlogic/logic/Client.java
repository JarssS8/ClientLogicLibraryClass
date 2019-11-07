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
import java.net.SocketException;
import java.util.logging.Logger;
import utilities.beans.User;
import utilities.beans.Message;
import utilities.exception.LoginAlreadyTakenException;
import utilities.exception.ServerConnectionErrorException;
import utilities.interfaces.Connectable;

/**
 * @author Gaizka Andrés
 */
public class Client implements Connectable {

    private static final Logger LOGGER = Logger.getLogger("clientlogic.logic.Client");
    private final int PORT = 5000;
    private final String IP = "localhost";
    Message message;
    Socket socket;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;

    public Client() {

        try {
            socket = new Socket(IP, PORT);
            socket.setSoTimeout(6000);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            LOGGER.warning("Server is off");
        }

    }
    
    public Client(String IP, int PORT) {

        try {
            socket = new Socket(IP, PORT);
            socket.setSoTimeout(6000);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            LOGGER.warning("Server is off");
        }

    }

    /**
     * Send a Login petition to the server
     *
     * @param user
     * @return
     * @throws utilities.exception.LoginNotFoundException
     * @throws utilities.exception.WrongPasswordException
     * @throws utilities.exception.ServerConnectionErrorException
     */
    @Override
    public User logIn(User user) throws LoginNotFoundException, WrongPasswordException, ServerConnectionErrorException {
        try {
            if (socket != null) {
                LOGGER.info("Login petition initialize");
                //Create a new message with the user who had received 
                message = new Message();
                message.setUser(user);
                message.setType("Login");
                //Send the message through the socket to the server
                objectOutputStream.writeObject(message);
                LOGGER.info("Sending message to server...");
                //Receive the response of the server through the socket
                message = (Message) objectInputStream.readObject();
                LOGGER.info("Reading message from server...");
                //Control of error of Login
                switch (message.getType()) {
                    case "LoginError":
                        throw new LoginNotFoundException();
                    case "PasswordError":
                        throw new WrongPasswordException();
                    case "ServerError":
                        throw new ServerConnectionErrorException();
                }
            } else {
                throw new ServerConnectionErrorException();
            }
        } catch (SocketException e) {
            throw new ServerConnectionErrorException();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.warning("Error de conexión al servidor" + e.getMessage());
            throw new ServerConnectionErrorException();
        }
        //Closing the socket
        try {
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
        } catch (IOException ex) {
            LOGGER.warning("Error de conexión al servidor" + ex.getMessage());
        }
        // Return of the message to the controller
        LOGGER.info("Returning the message");
        return message.getUser();

    }

    /**
     * Send a SignUp petition to the server
     *
     * @param user
     * @return
     * @throws utilities.exception.LoginAlreadyTakenException
     * @throws utilities.exception.ServerConnectionErrorException
     */
    @Override
    public User signUp(User user) throws LoginAlreadyTakenException, ServerConnectionErrorException {
        try {
            if (socket != null) {
                LOGGER.info("SignUp petition initialize");
                //Create a new message with the user who had received 
                message = new Message();
                message.setUser(user);
                message.setType("SignUp");
                //Send the message through the socket to the server
                objectOutputStream.writeObject(message);
                LOGGER.info("Sending message to server...");

                //Receive the response of the server through the socket
                message = (Message) objectInputStream.readObject();
                LOGGER.info("Reading message from server...");
                //Control of error of Sign Up
                switch (message.getType()) {
                    case "LoginTaken":
                        throw new LoginAlreadyTakenException();
                    case "ServerError":
                        throw new ServerConnectionErrorException();
                }
            } else {
                throw new ServerConnectionErrorException();
            }
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.warning("Server connection error: " + e.getMessage());
            throw new ServerConnectionErrorException();
        }
        try {
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
        } catch (IOException ex) {
            LOGGER.warning("Error closing streams" + ex.getMessage());
        }
        // Return of the message to the controller
        LOGGER.info("Returning the message");
        return message.getUser();
    }

    /**
     * Send a LogOut petition to the server
     *
     * @param user
     * @throws utilities.exception.ServerConnectionErrorException
     */
    @Override
    public void logOut(User user) throws ServerConnectionErrorException {
        LOGGER.info("LogOut petition initialize");
        try {
            //Create a new message with the user who had received 
            message = new Message();
            message.setUser(user);
            message.setType("LogOut");
            //Send the message through the socket to the server
            objectOutputStream.writeObject(message);
            LOGGER.info("Sending message to server...");

            //Receive the response of the server through the socket
            message = (Message) objectInputStream.readObject();
            LOGGER.info("Reading message from server...");

            //Closing streams
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.warning("Error de conexión al servidor" + e.getMessage());
            throw new ServerConnectionErrorException();
        }
    }

}
