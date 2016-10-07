/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared;

import RMIClient.RMIClient;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author jdone
 */
public class Receive extends Thread {

    private final String username;
    private final RMIClient c;
    private final JTextArea jTextAreaReceive;
    private String msgs = "";

    public Receive(String username, RMIClient c, JTextArea jTextAreaReceive) {
        this.username = username;
        this.c = c;
        this.jTextAreaReceive = jTextAreaReceive;
    }
    
    @Override
    public synchronized void run(){
        //c.clearCount();
        while (true) {
            String msg = c.receiveMessage();
            if (msg != null) {
                msgs += msg;
                jTextAreaReceive.setText(msgs);
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Receive.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getUsername() {
        return username;
    }
}
