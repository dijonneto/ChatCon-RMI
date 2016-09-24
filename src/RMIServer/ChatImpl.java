/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIServer;

import Security.RSA;
import Shared.Constants;
import Shared.IChat;
import Shared.User;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author jdone
 */
public class ChatImpl extends UnicastRemoteObject implements IChat {

    private static final long serialVersionUID = 1L;

    private ArrayList<User> users;
    private ArrayList<byte[]> messages;
    private int nMensagens;

    private SecretKeySpec key;
    private static final String keyValue = "ADBSJHJS12547896";

    public ChatImpl() throws RemoteException {
        super();
        this.messages = new ArrayList<>();
        this.users = new ArrayList<>();
        generateKeySymmetric();
    }

    public final void generateKeySymmetric() {
        try {
            key = new SecretKeySpec(keyValue.getBytes("UTF-8"), Constants.ALGORITHMs);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ChatImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public byte[] getKeySymmetric(PublicKey publicKey) throws RemoteException {
        // get base64 encoded version of the key
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        
        byte[] encryptedKey = RSA.encrypt(encodedKey, publicKey);
        return encryptedKey;
    }

    @Override
    public int identifyUser(String name) throws RemoteException {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(name)) {
                return i;
            }
        }
        User u = new User();
        u.setUsername(name);
        users.add(u);
        return users.size() - 1;
    }

    @Override
    public byte[] registerUser(User u, PublicKey key) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean loginUser(String username, String password) throws RemoteException {
        for (User user : users) {
            if (user.login(username, password)) {
                user.setOnline(true);
                return true;
            }
        }

        return false;
    }
    
    @Override
    public boolean logoutUser(int id) throws RemoteException {
        return false;
    }

    @Override
    public void sendMessage(byte[] msg) throws RemoteException {
        messages.add(msg);
    }

    @Override
    public ArrayList<byte[]> receiveMessage() throws RemoteException {
        return messages;
    }

}
