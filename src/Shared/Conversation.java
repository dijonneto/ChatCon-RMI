/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared;

import java.util.ArrayList;
import java.util.List;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author jdone
 */
public class Conversation {
    private final int idClient1;
    private final int idClient2;
    private final List<Message> messages;
    private final SecretKeySpec key;

    public Conversation(int idClient1, int idClient2, SecretKeySpec key) {
        this.idClient1 = idClient1;
        this.idClient2 = idClient2;
        this.messages = new ArrayList<>();
        this.key = key;
    }

    public int getIdClient1() {
        return idClient1;
    }

    public int getIdClient2() {
        return idClient2;
    }

    public SecretKeySpec getKey() {
        return key;
    }

    public Message getLastMessage() {
        return messages.get(messages.size()-1);
    }

    public List<Message> getMessages() {
        return messages;
    }
    
    public void addMessage(Message msg){
        messages.add(msg);
    }
    
    public boolean hasClient(int idClient){
        return idClient == idClient1 || idClient == idClient2;
    }
    
}
