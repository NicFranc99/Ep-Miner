package server;
import javafx.scene.layout.VBox;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author nicola francavilla
 * <p>Classe che modella un server in grado di accettare la richiesta trasmessa da un generico <b>Client</b> e istanza un oggetto della classe {@link server.ServerOneClient} che si
 * occuperà di servire le richieste del client in un thread dedicato. Tale <b>Server</b> sarà registrato su una porta predefinita (al di fuori del range 1-1024).</p>
 */
class MultiServer {
    /**
     * @uml.property name="PORT" porta su cui il server corrente è in ascolto
     */
    private static final int PORT = 8080;

    /**
     * <p>Invoca il metodo {@link #run(VBox)} passando come parametro il VBox in modo da poter settare una nuova label per i messaggi che il Server riceve/invia dal/al client</p>
     * @param vBox {@link javafx.scene.layout.VBox}
     * @throws IOException {@link IOException}
     */
    MultiServer(VBox vBox) throws IOException {
        run(vBox);
    }

    /**
     * <p>Assegna ad una variabile locale il riferimento ad una istanza della classe {@link ServerSocket} creata usando la porta {@link server.MultiServer#PORT} dove si ponte in attesa di richiesta
     * di connessione da parte di client in riposta alle quali viene restituito l'oggetto {@link Socket}.</p>
     *
     * @throws IOException {@link IOException} eccezzione lanciata in caso di errore di connessione tra le socket.
     */
            public void run(VBox vBox)throws IOException{
                    ServerSocket s = new ServerSocket(PORT);
                    ServerController.addLabelSystemMessage("Server Avviato", vBox,"-fx-background-color: rgb(36,133,29);");
                try {
                    while (true) {
                        Socket socket = s.accept();
                        try {
                            ServerOneClient server = new ServerOneClient(socket, vBox);
                        }catch (IOException ex) {
                        socket.close();
                        ex.fillInStackTrace();
                    }
                    }
                }
             finally {
                  s.close();
               }
                    }
            }

