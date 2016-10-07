/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.util.List;

/**
 *
 * @author jdone
 */
public interface IChat extends Remote {
    
    public int identifyUser(String name, PublicKey publicKey) throws RemoteException;
    
    public List<String> getOnlineUsers() throws RemoteException;
    
    public int initConversation(int idClient1, String nameClient2) throws RemoteException;
    
    public byte[] getKeySymmetric(int idConversation, int idClient) throws RemoteException;
    
    public void sendMessage(int idConversation, Message msg) throws RemoteException;

    public List<Integer> hasConversationForMe(int idClient) throws RemoteException;
    
    public List<Message> receiveMessages(int idConversation) throws RemoteException; // Message associa as mensagens a um remetente.
    
    public String getSenderUsername(int idSender) throws RemoteException;
    
    public void logoutUser(int id) throws RemoteException;
    
    // ---------------
    // Alterar abaixo:
    // ---------------
    
    // desnecessário //public int getIdReceiver(String username) throws RemoteException; // Array(String)clients | return Array(int)
    
    public byte[] registerUser(User u, PublicKey key) throws RemoteException;
    
    public boolean loginUser(String username, String password) throws RemoteException; 

}
