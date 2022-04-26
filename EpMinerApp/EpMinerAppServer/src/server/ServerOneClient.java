package server;

import data.Data;
import data.EmptySetException;
import database.DatabaseConnectionException;
import database.NoValueException;
import javafx.scene.layout.VBox;
import mining.EmergingPatternMiner;
import mining.FrequentPatternMiner;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

/**
 * @author nicola francavilla
 * Tale classe modella la comunicazione con più client.
 */
class ServerOneClient extends Thread {
    /**
     * @uml.property name="socket". Terminale lato server del canale tramite cui avviene lo scambio di oggetti client-server
     */
    private Socket socket;
    /**
     * @uml.property name="in". Flusso di oggetti in input al server (inviati dal client)
     */
    private ObjectInputStream in;
    /**
     * @uml.property name="out". Flusso di oggetti in output dal server al client.
     */
    private ObjectOutputStream out;
    private VBox vBox;
    private FrequentPatternMiner fpMiner;
    private EmergingPatternMiner epMiner;
    private Data dataTarget;
    private Data dataBackground;
    private String username;
    private String clientImagePath;
    private static String serverImagePath;
    /**
     * Inizializza il membro {@link server.ServerOneClient#socket} con il parametro in input al costruttore.
     * Inizializza {@link server.ServerOneClient#in} e {@link server.ServerOneClient#out}, avviando un <b>thread</b> invocando il metodo <b>start()</b>(Ereditato da {@link Thread})
     * @param s
     * @throws IOException eccezzione lanciata in caso di errore di connessione tra le socket.
     */
    ServerOneClient(Socket s,VBox vBox) throws IOException {
        this.socket = s;
        this.vBox = vBox;
        in = new ObjectInputStream(s.getInputStream());
        out = new ObjectOutputStream(s.getOutputStream());
        username = new String();
        clientImagePath = new String();
        start();
    }

    /**
     * Ridefinisce il metodo {@link Thread#run()} della classe {@link Thread}.
     * <p>Tale metodo gestisce le richieste del client</p>
     */
            @Override
            public void run() {
                try {
                    while (true) {
                        serverImagePath = "/Images/server.png";
                        if(clientImagePath.isEmpty()){
                            clientImagePath = (String)in.readObject();
                        }

                        if(username.isEmpty()){
                            username = (String)in.readObject();
                            ServerController.addLabelSystemMessage(username + " si è connesso!", vBox,"-fx-background-color: rgb(36,133,29);");
                        }
                        int option =  Integer.parseInt((String)in.readObject());
                        ServerController.addLabelClientMessage(String.valueOf(option), vBox,"-fx-background-color: rgb(233,233,235);",clientImagePath,username);
                        float minsup = Float.valueOf((String)in.readObject());
                        ServerController.addLabelClientMessage(String.valueOf(minsup), vBox,"-fx-background-color: rgb(233,233,235);",clientImagePath,username);
                        float minGr = Float.valueOf((String)in.readObject());
                        ServerController.addLabelClientMessage(String.valueOf(minGr), vBox,"-fx-background-color: rgb(233,233,235);",clientImagePath,username);
                        String nameTableTarget = (String) in.readObject();

                        ServerController.addLabelClientMessage(nameTableTarget, vBox,"-fx-background-color: rgb(233,233,235);",clientImagePath,username);
                        if(option == 1) {
                            try {
                                dataTarget = new Data(nameTableTarget);
                                fpMiner = new FrequentPatternMiner(dataTarget, minsup);
                                fpMiner.salva(nameTableTarget + minsup + ".dat");
                                out.writeObject(fpMiner.toString());
                                ServerController.addLableRightMessage(fpMiner.toString(),vBox);
                            } catch (SQLException | DatabaseConnectionException | NoValueException e) {
                                if (e instanceof NoValueException) {
                                    out.writeObject("ResultSet vuoto o il valore calcolato è pari a null!");
                                    ServerController.addLableRightMessage("ResultSet vuoto o il valore calcolato è pari a null!", vBox);
                                }
                                if (e instanceof SQLException) {
                                    out.writeObject("Non esiste alcuna tabella Target chiamata: " + nameTableTarget);
                                    ServerController.addLableRightMessage("Non esiste alcuna tabella Target chiamata: " + nameTableTarget, vBox);
                                }
                                if (e instanceof DatabaseConnectionException) {
                                    ServerController.addLableRightMessage("Non è stato possibile accedere al Database Controllare la connesione al Database...",vBox);
                                    out.writeObject("Non è stato possibile accedere al Database Controllare la connesione al Database...");
                                }
                                e.fillInStackTrace();
                            } catch (EmptySetException e) {
                                if (e instanceof EmptySetException) {
                                    out.writeObject("FrequentPattern Miner Vuoto!");
                                    ServerController.addLableRightMessage("FrequentPattern Miner Vuoto!", vBox);
                                }
                            }
                            String nameTableBackground = (String) in.readObject();
                            ServerController.addLabelClientMessage(nameTableBackground, vBox,"-fx-background-color: rgb(233,233,235);",clientImagePath,username);
                            try{
                                dataBackground = new Data(nameTableBackground);
                                if(fpMiner != null)
                                epMiner = new EmergingPatternMiner(dataBackground, fpMiner, minGr);
                                else throw new NullPointerException();
                                epMiner.salva(nameTableBackground + minsup + "_minGr" + minGr + ".dat");
                                out.writeObject(epMiner.toString());
                                ServerController.addLableRightMessage(epMiner.toString(),vBox);
                            }catch(SQLException | NullPointerException | DatabaseConnectionException | EmptySetException  | NoValueException ex){
                                if (ex instanceof SQLException) {
                                    out.writeObject("Non esiste alcuna tabella Background chiamata: " + nameTableBackground);
                                    ServerController.addLableRightMessage("Non esiste alcuna tabella Background chiamata: " + nameTableBackground, vBox);
                                }
                                if (ex instanceof EmptySetException) {
                                    out.writeObject("EmerginPattern Miner Vuoto!");
                                    ServerController.addLableRightMessage("EmerginPattern Miner Vuoto!", vBox);
                                }
                                if(ex instanceof NullPointerException) {
                                    out.writeObject("Non riesco a continuare con il calcolo se il nome della tabella target è sbagliato!");
                                    ServerController.addLableRightMessage("Non riesco a continuare con il calcolo se il nome della tabella target è sbagliato!",vBox);
                                }
                                if (ex instanceof DatabaseConnectionException) {
                                    out.writeObject("Non è stato possibile accedere al Database Controllare la connessione al Database...");
                                    ServerController.addLableRightMessage("Non è stato possibile accedere al Database Controllare la connesione al Database...",vBox);
                                }
                                if (ex instanceof NoValueException) {
                                    out.writeObject("ResultSet vuoto o il valore calcolato è pari a null!");
                                    ServerController.addLableRightMessage("ResultSet vuoto o il valore calcolato è pari a null!", vBox);
                                }
                                ex.fillInStackTrace();
                            }
                        }else {
                            try{
                                FrequentPatternMiner fpMiner = FrequentPatternMiner.carica(nameTableTarget + minsup + ".dat");
                                out.writeObject(fpMiner.toString());
                                ServerController.addLableRightMessage(fpMiner.toString(),vBox);
                            } catch (ClassNotFoundException
                                    | IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                out.writeObject("Impossibile trovare il file speficato per la tabella di target");
                                ServerController.addLableRightMessage("Impossibile trovare il file speficato per la tabella di target",vBox);
                            }
                            try {
                                String nameTableBackground = (String) in.readObject();
                                ServerController.addLabelClientMessage(nameTableBackground, vBox,"-fx-background-color: rgb(233,233,235);",clientImagePath,username);
                                EmergingPatternMiner epMiner = EmergingPatternMiner.carica(nameTableBackground + minsup + "_minGr" + minGr + ".dat");
                                out.writeObject(epMiner.toString());
                                ServerController.addLableRightMessage(epMiner.toString(),vBox);
                            } catch (ClassNotFoundException
                                    | IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                out.writeObject("Impossibile trovare il file speficato per la tabella di background");
                                ServerController.addLableRightMessage("Impossibile trovare il file speficato per la tabella di background",vBox);
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException err) {
                    err.fillInStackTrace();
                }
            }
}


