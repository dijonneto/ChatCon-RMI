/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 *
 * @author jdone
 */
public interface IChat extends Remote {
    
    public int identifyUser(String name) throws RemoteException;
    
    public byte[] getKeySymmetric(PublicKey key) throws RemoteException;
    
    public byte[] registerUser(User u, PublicKey key) throws RemoteException;
    
    public boolean loginUser(String username, String password) throws RemoteException;
    
    public boolean logoutUser(int id) throws RemoteException;

    public void sendMessage(byte[] msg) throws RemoteException;

    public ArrayList<byte[]> receiveMessage() throws RemoteException;

}
