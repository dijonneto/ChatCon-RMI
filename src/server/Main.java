/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import server.security.DesCryptoSystem;

/**
 *
 * @author jdone
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            SecretKey key = KeyGenerator.getInstance("DES").generateKey();
            DesCryptoSystem encrypter = new DesCryptoSystem(key);
            String encrypted = encrypter.encrypt("Don't tell anybody!");
            String decrypted = encrypter.decrypt(encrypted);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
