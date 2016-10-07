/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIClient;

import Security.AES;
import Security.RSA;
import Shared.Constants;
import Shared.IChat;
import Shared.Message;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author jdone
 */
public class RMIClient {

    private AES encrypter;
    private KeyPair keyPair;
    private SecretKey keySymmetric;
    private IChat stub;
    private int cont = 0;
    private String username;
    private String pairName = "";
    private int clientID = -1;
    private int idConversation;

    public RMIClient(String username) {
        try {
            stub = (IChat) Naming.lookup("rmi://127.0.0.1:1099/Chat");
            this.username = username;
            keyPair = RSA.generateKey();
            clientID = stub.identifyUser(username, keyPair.getPublic());
        } catch (Exception e) {
            System.err.println("Exceção no cliente: " + e.toString());
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public int getClientID() {
        return clientID;
    }

    public List<String> getOnlineUsers() {
        try {
            return stub.getOnlineUsers();
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void initConversation(String nameClient2) {
        try {
            pairName = nameClient2;
            idConversation = stub.initConversation(clientID, nameClient2);
            getKeySymmetric(idConversation);
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getKeySymmetric(int idConversation) throws RemoteException {
        byte[] encryptedKey = stub.getKeySymmetric(idConversation, clientID);
        keySymmetric = getSecretKey(encryptedKey, keyPair.getPrivate());
        encrypter = new AES(keySymmetric);
    }

    private SecretKey getSecretKey(byte[] encryptedKey, PrivateKey privateKey) {
        String decryptedKey = RSA.decrypt(encryptedKey, privateKey);
        // decode the base64 encoded string
        byte[] decodedKey = Base64.getDecoder().decode(decryptedKey);
        //System.out.println("Chave depois da transmissão: " + Arrays.toString(decodedKey));
        // rebuild key using SecretKeySpec
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, Constants.ALGORITHMs);
        return originalKey;
    }

    public synchronized void sendMessage(String message) {
        try {
            //if (!message.equals("fim")) {
            byte[] encMsg = encrypter.encrypt(message);
            System.out.println("Conversa: " + idConversation + ", Mensagem encriptada enviada: " + Arrays.toString(encMsg));
            stub.sendMessage(idConversation, new Message(clientID, encMsg));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public synchronized String receiveMessage() {
        try {
            //List<Integer> convs = stub.hasConversationForMe(clientID);
            //if (!convs.isEmpty()) {
                //for (Integer conv : convs) {
                    getKeySymmetric(idConversation); //conv
                    List<Message> msgs = stub.receiveMessages(idConversation); //conv
                    String decryptedMsg = "";
                    if (!msgs.isEmpty() && msgs.size() > cont) {
                        for (int i = msgs.size() - 1; i >= 0; i--) {
                        //for (int i = 0; i < msgs.size(); i++) {
                            byte[] encryptedMsg = msgs.get(i).getEncryptedMsg();
                            System.out.println("Conversa: " + idConversation);
                            System.out.println("Mensagem encriptada recebida: " + Arrays.toString(encryptedMsg));
                            int idSender = msgs.get(i).getIdSender();
                            decryptedMsg += stub.getSenderUsername(idSender) + " disse: \n" + encrypter.decrypt(encryptedMsg) + "\n\n";
                            System.out.println("Contador de mensagens: " + cont);
                            cont++;
                            System.out.println("Mensagem Recebida e Decriptada:\n" + decryptedMsg);
                            return decryptedMsg;
                        }
                    }
                //}
            //}
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return null;
    }
    
    public void clearCount(){
        cont = 0;
    }
    
    public String getPairName(){
        return pairName;
    }
    
    public void logoutUser(){
        try {
            stub.logoutUser(clientID);
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }           
    
}
