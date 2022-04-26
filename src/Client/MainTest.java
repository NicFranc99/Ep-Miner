package Client;

import java.io.*;
import java.net.Socket;
import javafx.scene.layout.VBox;
/**
 * @author map tutor
 */
public class MainTest {
    private VBox vBox;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public MainTest(VBox vBox, Socket socket) throws IOException {
        this.vBox = vBox;
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        System.out.println("Client creato");
    }


    void closeSocket() throws IOException, InterruptedException {
        System.out.println("Chiusura del client..");
        this.socket.close();
        Thread.sleep(1000);
        System.exit(0);
    }


    void sendMessageToServer(String message) {
        try {
            out.writeObject(message);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

        String receiveMessageToServer() throws  IOException,ClassNotFoundException {
             return (String) (this.in.readObject());
    }
}
