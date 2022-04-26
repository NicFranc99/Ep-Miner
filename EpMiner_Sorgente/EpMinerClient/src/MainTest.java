import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import keyboardinput.Keyboard;

/**
 * @author map tutor
 */
public class MainTest {

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        try {
        Socket socket;
        if(args.length != 0){
           InetAddress addr = InetAddress.getByName(args[0]);
            System.out.println("addr = " + addr + "\nport=" + args[1]);
            socket = new Socket(addr, new Integer(args[1]));
        }else{
            InetAddress addr = InetAddress.getByName("127.0.0.1");
            final int PORT = 8080;
            System.out.println("addr = " + addr + "\nport=" + PORT);
            socket = new Socket(addr, PORT);
        }



          /*  socket = new Socket(addr, new Integer(args[1]));*/
            System.out.println(socket);


            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ; // stream con richieste del client


            char risp = 's';
            do {
                System.out.println("Scegli una opzione:");
                int opzione;
                do {
                    System.out.println("1:Nuova scoperta");
                    System.out.println("2: Risultati in archivio");
                    opzione = Keyboard.readInt();

                } while (opzione != 1 && opzione != 2);

                float minsup = 0f;
                float minGr = 0f;
                do {
                    System.out.println("Inserire valore minimo supporto (minsup>0 e minsup<=1):");
                    minsup = Keyboard.readFloat();
                } while (minsup <= 0 || minsup > 1);

                do {
                    System.out.println("Inserire valore minimo grow rate (minGr>0):");
                    minGr = Keyboard.readFloat();
                } while (minGr <= 0);

                System.out.println("Tabella target:");
                String targetName = Keyboard.readString();
                System.out.println("Tabella background:");
                String backgroundName = Keyboard.readString();
                String nameFile = targetName + "_" + backgroundName;

                try {
                    out.writeObject(opzione);
                    out.writeObject(minsup);
                    out.writeObject(minGr);
                    out.writeObject(targetName);
                    out.writeObject(backgroundName);

                    String fpMiner = (String) (in.readObject());

                    System.out.println(fpMiner);

                    String epMiner = (String) (in.readObject());

                    System.out.println(epMiner);
                } catch (IOException | ClassNotFoundException e) {
                    if (e instanceof IOException) {
                        System.out.println("Il server sembra non rispondere...");
                        break;
                    }

                }

                System.out.println("Vuoi ripetere?(s/n)");
                risp = Keyboard.readChar();
            } while (risp != 'n');

            System.out.println("Chiusura del client..");
            socket.close();

        } catch (ConnectException ex) {
            System.out.println("Nessun Server in ascolto. Riprovare");
        }
    }

}
