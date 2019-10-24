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
    public static Connectable getClient(){
        return new Client();
    }
}
