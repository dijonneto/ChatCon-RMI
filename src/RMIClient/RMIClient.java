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
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Scanner;
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

    RMIClient() {
        try {
            IChat stub = (IChat) Naming.lookup("rmi://127.0.0.1:1099/Chat");
            //System.out.println("Resposta: " + stub.receiveMessage());
            //final ServidorChat chat = (ServidorChat) Naming.lookup("rmi://192.168.102.11:1098/ServidorChat");

            keyPair = RSA.generateKey();

            byte[] encryptedKey = stub.getKeySymmetric(keyPair.getPublic());
            keySymmetric = getSecretKey(encryptedKey, keyPair.getPrivate());
            encrypter = new AES(keySymmetric);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Digite o nome do usuário:");
            String username = scanner.nextLine();
            int clientID = stub.identifyUser(username);

            String msg = scanner.nextLine();
            while (!msg.equals("fim")) {
                byte[] encMsg = encrypter.encrypt(msg);
                stub.sendMessage(encMsg);//clientID + Constants.DELIMITER + msg);
                // System.out.println(chat.lerMensagem().get(cont));
                msg = scanner.nextLine();
            }

            Thread thread = new Thread(new Runnable() {
                int cont = stub.receiveMessage().size();

                @Override
                public void run() {
                    try {
                        for (byte[] encryptedMsg : stub.receiveMessage()) {
                            String decryptedMsg = encrypter.decrypt(encryptedMsg);
                            System.out.println(decryptedMsg);
                            cont++;
                        }
                        
//                        while (true) {
//                            if (stub.receiveMessage().size() >= cont) {
//                                byte[] encryptedMsg = stub.receiveMessage().get(stub.receiveMessage().size() - 1);
//                                String decryptedMsg = encrypter.decrypt(encryptedMsg);
//                                System.out.println(decryptedMsg);
//                                cont++;
//                            }
//                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
            );
            thread.start();

        } catch (Exception e) {
            System.err.println("Exceção no cliente: " + e.toString());
            e.printStackTrace();
        }
    }

    private SecretKey getSecretKey(byte[] encryptedKey, PrivateKey privateKey) {
        String decryptedKey = RSA.decrypt(encryptedKey, privateKey);
        // decode the base64 encoded string
        byte[] decodedKey = Base64.getDecoder().decode(decryptedKey);
        // rebuild key using SecretKeySpec
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, Constants.ALGORITHMs);
        return originalKey;
    }

    public static void main(String args[]) {
        new RMIClient();
    }
}
