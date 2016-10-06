/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared;

import java.io.Serializable;

/**
 *
 * @author jdone
 */
public class Message implements Serializable{
    private int idSender;
    private byte[] encryptedMsg;

    public Message(int idSender, byte[] encryptedMsg) {
        this.idSender = idSender;
        this.encryptedMsg = encryptedMsg;
    }

    public int getIdSender() {
        return idSender;
    }

    public byte[] getEncryptedMsg() {
        return encryptedMsg;
    }
    
}
