package messenger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

public class ChatServer {
    public static void main(String[] args) throws Exception {
        String send_from_client;
        String send_to_client;

        ServerSocket welcomeSocket = new ServerSocket(8088);
        AsymmetricCryptography ac = new AsymmetricCryptography();
        PrivateKey privateKey = ac.getPrivate("Key/serverPrivateKey");
        PublicKey publicKey = ac.getPublic("Key/clientPublicKey");
        while(true) {
            Socket connectionSocket = welcomeSocket.accept();

            BufferedReader inFromClient =
                    new BufferedReader(new
                            InputStreamReader(connectionSocket.getInputStream()));

            DataOutputStream outToClient =
                    new DataOutputStream(connectionSocket.getOutputStream());

            send_from_client = inFromClient.readLine();
            String decrypted_msg = ac.decryptText(send_from_client, publicKey);

            send_to_client = decrypted_msg;
            String encrypted_msg = ac.encryptText(send_to_client, privateKey);

            outToClient.writeBytes(encrypted_msg + '\n');

            System.out.println(decrypted_msg);

            if (send_from_client.equalsIgnoreCase("bye")) {
                return;
            }
        }
    }
}
