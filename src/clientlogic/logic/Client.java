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
 * This class implements Connectable interface. Creates a socket and connects
 * with server side.
 *
 * @author Gaizka Andrés
 */
public class Client implements Connectable {

    private static final Logger LOGGER = Logger.getLogger("clientlogic.logic.Client");
    private int port = 5000;
    private String ip = "localhost";
    Message message;
    Socket socket;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;

    /**
     * An empty constructor for the client.
     */
    public Client() {
    }

    /**
     * A constructor with two parameters for the client.
     *
     * @param ip a String that contains the IP
     * @param port an int that contains the Port
     */
    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void socketCreation() {
        try {
            socket = new Socket(ip, port);
            LOGGER.info("Socket created");
            socket.setSoTimeout(6000);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            LOGGER.warning("Server is OFF");
        }
    }

    /**
     * This class sends a Login petition to the server.
     *
     * @param user a User object that contains the data saved from the login
     * window.
     * @return a user with the data recovered from the database.
     * @throws LoginNotFoundException If login does not exist in the database.
     * @throws WrongPasswordException If password does not match with the user.
     * @throws ServerConnectionErrorException If there's an error in the server.
     */
    @Override
    public User logIn(User user) throws LoginNotFoundException,
            WrongPasswordException, ServerConnectionErrorException {
        socketCreation();
        try {
            if (socket != null) {
                LOGGER.info("Beginning login request...");
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
            LOGGER.warning("Client: Socket connection error" + e.getMessage());
            throw new ServerConnectionErrorException();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.warning("Client: Server connection error" + e.getMessage());
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
            LOGGER.warning("Client: Server connection error" + ex.getMessage());
        }
        // Return of the message to the controller
        LOGGER.info("Returning the message");
        return message.getUser();

    }

    /**
     * This class sends a Sign up petition to the server.
     *
     * @param user a User object that contains the data saved from the sign up.
     * @return a user with the data recovered from the database.
     * @throws LoginAlreadyTakenException If the login already exists in the
     * database.
     * @throws ServerConnectionErrorException If there's an error in the server.
     */
    @Override
    public User signUp(User user) throws LoginAlreadyTakenException, ServerConnectionErrorException {
        socketCreation();
        try {
            if (socket != null) {
                LOGGER.info("Beginning SignUp request...");
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
            LOGGER.warning("Client: Server connection error: " + e.getMessage());
            throw new ServerConnectionErrorException();
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException ex) {
                LOGGER.warning("Client: Error closing streams" + ex.getMessage());
            }
        }

        // Return of the message to the controller
        LOGGER.info("Returning the message");
        return message.getUser();
    }

    /**
     * This class sends a Log out petition to the server.
     *
     * @param user a User object that contains the data saved from the sign up.
     * @throws ServerConnectionErrorException If there's an error in the server.
     */
    @Override
    public void logOut(User user) throws ServerConnectionErrorException {
        LOGGER.info("Beginning logout request...");
        socketCreation();
        try {
            if (socket != null) {
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
            }
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.warning("Error de conexión al servidor" + e.getMessage());
            throw new ServerConnectionErrorException();
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException ex) {
                LOGGER.warning("Client: Error closing streams" + ex.getMessage());
            }
        }
    }

}
