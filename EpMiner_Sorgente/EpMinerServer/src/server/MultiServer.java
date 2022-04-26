package server;
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

    public static void main(String[] args) {
        try {
            MultiServer server = new MultiServer();
        } catch (IOException exception) {
            exception.fillInStackTrace();
        }
    }

     private MultiServer() throws IOException {
        run();
    }

    /**
     * <p>Assegna ad una variabile locale il riferimento ad una istanza della classe {@link ServerSocket} creata usando la porta {@link server.MultiServer#PORT} dove si ponte in attesa di richiesta
     * di connessione da parte di client in riposta alle quali viene restituito l'oggetto {@link Socket}.</p>
     *
     * @throws IOException eccezzione lanciata in caso di errore di connessione tra le socket.
     */
    private void run() throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Server avviato");
        while (true) {
            Socket socket = s.accept();
            try {
                ServerOneClient server = new ServerOneClient(socket);
                System.out.println("Connessione di " + socket);
                System.out.println("Nuovo client connesso");
            } catch (IOException ex) {
                socket.close();
                ex.fillInStackTrace();
            }
        }
    }
}

