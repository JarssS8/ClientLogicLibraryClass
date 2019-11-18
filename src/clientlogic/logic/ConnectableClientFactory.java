/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientlogic.logic;

import utilities.interfaces.Connectable;

/**
 * This class creates a new Client.
 * @author Gaizka
 * 
 */
public class ConnectableClientFactory {
    /**
     * This method returns a Connectable implementation with an IP and a PORT.
     * @param IP A string that contains the IP to connect.
     * @param PORT An int that contains the PORT to connect.
     * @return a Connectable implementation.
     */
    public static Connectable getClient(String IP, int PORT){
        return new Client(IP,PORT);
    }
    
    /**
     * This method returns a Connectable implementation.
     * @return a Connectable implementation.
     */
    public static Connectable getClient(){
        return new Client();
    }
}
