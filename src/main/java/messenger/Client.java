package messenger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Client {
    public static void main(String argv[]) throws Exception
    {
        String send_to_server;
        String send_from_server;
        AsymmetricCryptography ac = new AsymmetricCryptography();
        PrivateKey privateKey = ac.getPrivate("Key/clientPrivateKey");
        PublicKey publicKey = ac.getPublic("Key/serverPublicKey");
        while (true) {
            System.out.print("Input: ");
            BufferedReader inFromUser =
                    new BufferedReader(new InputStreamReader(System.in));
            send_to_server = inFromUser.readLine();
            Socket clientSocket = new Socket("127.0.0.1", 8088);

            DataOutputStream outToServer =
                    new DataOutputStream(clientSocket.getOutputStream());

            BufferedReader inFromServer =
                    new BufferedReader(new
                            InputStreamReader(clientSocket.getInputStream()));

            String encrypted_msg = ac.encryptText(send_to_server, privateKey);
            outToServer.writeBytes(encrypted_msg + '\n');

            send_from_server = inFromServer.readLine();
            String decrypted_msg = ac.decryptText(send_from_server, publicKey);

            System.out.println(decrypted_msg);

            clientSocket.close();
            if (decrypted_msg.equalsIgnoreCase("bye")) {
                return;
            }
        }
    }
}
