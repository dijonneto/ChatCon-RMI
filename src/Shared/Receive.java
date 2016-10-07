/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared;

import RMIClient.RMIClient;
import javax.swing.JTextArea;

/**
 *
 * @author jdone
 */
public class Receive extends Thread {

    private final RMIClient c;
    private final JTextArea jTextAreaReceive;

    public Receive(RMIClient c, JTextArea jTextAreaReceive) {
        this.c = c;
        this.jTextAreaReceive = jTextAreaReceive;
    }
    
    @Override
    public synchronized void run(){
        String msgs = "";
        c.clearCount();
        while (true) {
            String msg = c.receiveMessage();
            if (msg != null) {
                msgs += msg;
                jTextAreaReceive.setText(msgs);
            }
        }
    }
}
