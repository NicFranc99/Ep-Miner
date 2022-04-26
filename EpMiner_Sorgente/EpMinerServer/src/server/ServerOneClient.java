package server;

import data.Data;
import data.EmptySetException;
import database.DatabaseConnectionException;
import database.NoValueException;
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

    /**
     * Inizializza il membro {@link server.ServerOneClient#socket} con il parametro in input al costruttore.
     * Inizializza {@link server.ServerOneClient#in} e {@link server.ServerOneClient#out}, avviando un <b>thread</b> invocando il metodo <b>start()</b>(Ereditato da {@link Thread})
     *
     * @param s
     * @throws IOException eccezzione lanciata in caso di errore di connessione tra le socket.
     */
    ServerOneClient(Socket s) throws IOException {
        this.socket = s;
        in = new ObjectInputStream(s.getInputStream());
        out = new ObjectOutputStream(s.getOutputStream());
        start();
    }

    /**
     * Ridefinisce il metodo {@link Thread#run()} della classe {@link Thread}.
     * <p>Tale metodo gestisce le richieste del client</p>
     */
    public void run() {
        try {
            while (true) {
                int option = (int) in.readObject();
                float minsup = (float) in.readObject();
                float minGr = (float) in.readObject();
                String nameTableTarget = (String) in.readObject();
                String nameTableBackground = (String) in.readObject();

                if (option == 1) {
                    try {
                        Data dataTarget = new Data(nameTableTarget);
                        Data dataBackground = new Data(nameTableBackground);
                        try {
                            FrequentPatternMiner fpMiner = new FrequentPatternMiner(dataTarget, minsup);
                            fpMiner.salva(nameTableTarget + minsup + ".dat");
                            out.writeObject(fpMiner.toString());
                            try {
                                EmergingPatternMiner epMiner = new EmergingPatternMiner(dataBackground, fpMiner, minGr);
                                epMiner.salva(nameTableBackground + minsup + "_minGr" + minGr + ".dat");
                                out.writeObject(epMiner.toString());
                            } catch (EmptySetException e) {
                                out.writeObject("EmerginPattern Miner Vuoto!");
                            }
                        } catch (EmptySetException | ClassCastException | IOException e) {
                            if (e instanceof EmptySetException)
                                out.writeObject("FrequentPattern Miner Vuoto!");
                            e.fillInStackTrace();
                        }
                    } catch (SQLException | DatabaseConnectionException | NoValueException e) {
                        if (e instanceof SQLException) {
                            out.writeObject("Errore");
                            out.writeObject("Errore");
                        }
                        if (e instanceof DatabaseConnectionException) {
                            System.err.println("Errore di connessione al Database" + ((DatabaseConnectionException) e).getMessage());
                            out.writeObject("Non è stato possibile accedere al Database");
                            out.writeObject(((DatabaseConnectionException) e).getMessage());
                        }

                        if (e instanceof NoValueException)
                            System.err.println("ResultSet vuoto o il calore calcolato è pari a null!" + ((NoValueException) e).getMessage());
                        e.fillInStackTrace();
                    }

                } else {
                    try {
                        FrequentPatternMiner fpMiner = FrequentPatternMiner.carica(nameTableTarget + minsup + ".dat");
                        out.writeObject(fpMiner.toString());
                    } catch (ClassNotFoundException
                            | IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        out.writeObject("Impossibile trovare il file speficato per la tabella di target");
                    }
                    try {
                        EmergingPatternMiner epMiner = EmergingPatternMiner.carica(nameTableBackground + minsup + "_minGr" + minGr + ".dat");
                        out.writeObject(epMiner.toString());
                    } catch (ClassNotFoundException
                            | IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        out.writeObject("Impossibile trovare il file speficato per la tabella di background");
                    }
                }
            }
        } catch (IOException | ClassNotFoundException err) {
            err.fillInStackTrace();
        }
    }
}


