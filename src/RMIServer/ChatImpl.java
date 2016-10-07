/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIServer;

import Security.RSA;
import Shared.Constants;
import Shared.Conversation;
import Shared.IChat;
import Shared.Message;
import Shared.User;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author jdone
 */
public class ChatImpl extends UnicastRemoteObject implements IChat {

    private static final long serialVersionUID = 1L;

    private final List<User> users;
    private final List<Conversation> conversations;
    private int nMensagens;

    //private SecretKeySpec key;
    //private static final String keyValue = "ADBSJHJS12547896";
    public ChatImpl() throws RemoteException {
        super();
        this.conversations = new ArrayList<>();
        this.users = new ArrayList<>();
        generateKeySymmetric();
    }

    @Override
    public int identifyUser(String name, PublicKey publicKey) throws RemoteException {
        int id = getIdClient(name);
        if (id != -1) {
            System.out.println("Usuário identificado: " + name + ", ID: " + id);
            return id;
        }

        System.out.println("Usuário adicionado: " + name + ", ID: " + users.size());
        User u = new User();
        u.setUsername(name);
        u.setPublicKey(publicKey);
        u.setOnline(true);
        users.add(u);
        return users.size() - 1;
    }

    private int getIdClient(String username) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public List<String> getOnlineUsers() throws RemoteException {
        List<String> listOnlineUsers = new ArrayList<>();
        for (User user : users) {
            if (user.isOnline()) {
                listOnlineUsers.add(user.getUsername());
            }
        }

        return listOnlineUsers;
    }

    @Override
    public int initConversation(int idClient1, String nameClient2) throws RemoteException {
        int idClient2 = getIdClient(nameClient2);

        for (int i = 0; i < conversations.size(); i++) {
            if (conversations.get(i).hasClient(idClient1) && conversations.get(i).hasClient(idClient2)) {
                return i;
            }
        }

        System.out.println("Conversa adicionada: IdConv: " + conversations.size() + " IdC1: " + idClient1 + ", IdC2: " + idClient2);

        conversations.add(new Conversation(idClient1, idClient2, generateKeySymmetric()));
        return conversations.size() - 1;
    }

    public final SecretKeySpec generateKeySymmetric() {
        SecretKeySpec key = null;
        try {
            key = new SecretKeySpec(generateRandomChars().getBytes("UTF-8"), Constants.ALGORITHMs); //keyValue
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ChatImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return key;
    }

    public static String generateRandomChars() {
        String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        int length = 16;

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars
                    .length())));
        }

        return sb.toString();
    }

    @Override
    public byte[] getKeySymmetric(int idConversation, int idClient) throws RemoteException {
        // TODO pegar chave simétrica da conversa.
        // get base64 encoded version of the key
        byte[] key = conversations.get(idConversation).getKey().getEncoded();
        //System.out.println("Chave antes da transmissão: " + Arrays.toString(key));
        String encodedKey = Base64.getEncoder().encodeToString(key);

        PublicKey publicKey = users.get(idClient).getPublicKey();

        byte[] encryptedKey = RSA.encrypt(encodedKey, publicKey);
        return encryptedKey;
    }

    @Override
    public void sendMessage(int idConversation, Message msg) throws RemoteException {
        conversations.get(idConversation).addMessage(msg);
        System.out.println("Mensagem enviada: Conversa: " + idConversation + ", Remetente: " + msg.getIdSender() + ", Mensagem Encriptda: " + Arrays.toString(msg.getEncryptedMsg()));
    }

    @Override
    public List<Integer> hasConversationForMe(int idClient) throws RemoteException {
        List<Integer> listIdConversations = new ArrayList<>();

        for (int i = 0; i < conversations.size(); i++) {
            if (conversations.get(i).hasClient(idClient)) {
                listIdConversations.add(i);
            }
        }

        return listIdConversations;
    }

    @Override
    public List<Message> receiveMessages(int idConversation) throws RemoteException {
        return conversations.get(idConversation).getMessages();
    }

    @Override
    public String getSenderUsername(int idSender) throws RemoteException {
        return users.get(idSender).getUsername();
    }

    // ---------------------------------------------------------------- //
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
}
