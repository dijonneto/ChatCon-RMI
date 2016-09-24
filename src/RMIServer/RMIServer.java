/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIServer;

import Shared.IChat;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 *
 * @author jdone
 */
public class RMIServer {

    RMIServer() {
        try{
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            LocateRegistry.createRegistry(1099);
            IChat stub = new ChatImpl();
            Naming.bind("Chat", (Remote)stub);
            System.out.println("Servidor no ar!");
        } catch (RemoteException | AlreadyBoundException | MalformedURLException e){
            System.err.println("Exceção no servidor: " + e.toString());
            e.printStackTrace();
        }
    }
        
    public static void main(String[] args) {
        new RMIServer();
    }
}
