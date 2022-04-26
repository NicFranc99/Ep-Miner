package Client;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    @FXML
    private Button button_send;
    @FXML
    private TextField tf_message = new TextField();
    @FXML
    private VBox vbox_messages;
    @FXML
    private ScrollPane sp_main;
    private MainTest client;
    private int opzione = 0;
    private float minSup = 0f;
    private float minGr = 0f;
    private String targetTable = new String();
    private String backgroundTable = new String();
    private String risp = new String();
    private boolean clientError = false;
    private static String username;
    private static String address;
    private static int port;
    private static String imagePath;
    private static boolean firstRun = true;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        new Thread(() -> {
            try {
                addLableLeftMessage("Hello: "+ username , vbox_messages, "-fx-background-color: rgb(36,133,29);");
                Socket socket = new Socket(InetAddress.getByName(address), port);
                client = new MainTest(vbox_messages, socket);
                if(firstRun) {
                    client.sendMessageToServer(imagePath);
                    client.sendMessageToServer(username);
                }
                firstRun = false;
                addLableLeftMessage("Scegli una opzione:" + "\n" + "1:Nuova scoperta" + "\n" + "2: Risultati in archivio", vbox_messages, "-fx-background-color: rgb(36,133,29);");
            } catch (IOException exception) {
                if(exception instanceof ConnectException){
                    Platform.runLater(() -> {
                        Alert dialog = new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK);
                        dialog.setHeaderText(exception.getClass().getName());
                        dialog.setContentText("Errore! Non c'è nessun server in ascolto sulla porta: " + port);
                        dialog.show();
                    });
                    ClientController.addLableLeftMessage("Errore! Non c'è nessun server in ascolto sulla porta: " + port,vbox_messages,"-fx-background-color: rgb(255,0,0);");
                    ClientController.addLableLeftMessage("Riprovare...",vbox_messages,"-fx-background-color: rgb(255,0,0);");
                }
                else if(exception instanceof UnknownHostException | exception instanceof SocketException) {
                    Platform.runLater(() -> {
                        Alert dialog = new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK);
                        dialog.setHeaderText(exception.getClass().getName());
                        dialog.setContentText("Errore! L'indirizzo Ip inserito è errato!" + address);
                        dialog.show();
                    });
                }
                System.err.println(exception);
                ClientController.addLableLeftMessage("Vuoi chiudere la chat?(s/n)", vbox_messages, "-fx-background-color: rgb(36,133,29);");
                clientError = true;
            }
        }).start();

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                sp_main.setVvalue((Double ) t1);
            }
        });


//Metodo che fa in modo che il bottone quando viene cliccato, il messaggio che viene scritto nella TextArea viene mandato al client
            button_send.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    String messageToSend = tf_message.getText();
                    String value;
                    if (!messageToSend.isEmpty() && messageToSend != null) {
                        value = messageToSend;
                        HBox hBoxTime = new HBox();
                        hBoxTime.setAlignment(Pos.TOP_RIGHT);
                        hBoxTime.setPadding(new Insets(5,5,1,10));
                        Text time = new Text(getCurrentTime());
                        time.setFill(Color.BLACK);
                        time.setStyle("-fx-font: 10 arial;");
                        TextFlow timeFlow = new TextFlow(time);
                        hBoxTime.getChildren().add(timeFlow);

                        Text usernameText = new Text(username);
                        usernameText.setFill(Color.BLACK);
                        usernameText.setStyle("-fx-font: 10 arial;");
                        TextFlow flowUsername = new TextFlow(usernameText);
                        flowUsername.setPadding(new Insets(5,10,5,10));

                        HBox hBox = new HBox();
                        hBox.setAlignment(Pos.CENTER_RIGHT);
                        hBox.setPadding(new Insets(1, 5, 5, 10));
                        Text text = new Text(messageToSend);
                        TextFlow textFlow = new TextFlow(text);
                        //Imposto il colore e il background color del testo da mandare al client (visualizzato nel server)
                        textFlow.setStyle("-fx-color:rgb(239,242,255);" + "-fx-background-color: rgb(15,12,242);" + "-fx-background-radius:20px;");
                        textFlow.setPadding(new Insets(5, 10, 5, 10));
                        text.setFill(Color.color(0.934, 0.945, 0.996));
                        hBox.getChildren().add(flowUsername);
                        hBox.getChildren().add(textFlow);
                        vbox_messages.getChildren().add(hBoxTime);
                        vbox_messages.getChildren().add(hBox);

                        Image img = new Image(imagePath);
                        ImageView imgView = new ImageView(img);
                        imgView.setFitHeight(30);
                        imgView.setFitWidth(30);
                        Circle circle = new Circle(0, 0, 30);
                        ImagePattern pattern = new ImagePattern(new Image(imagePath, 280, 180, false, false));
                        circle.setFill(pattern);
                        circle.setEffect(new InnerShadow(10, Color.BLACK));  // Shadow
                        hBox.getChildren().add(imgView);
                            if(opzione == 0 && !clientError){
                                try {
                                    int number = Integer.parseInt(value);
                                    if(number != 1 && number != 2)
                                        ClientController.addLableLeftMessage("Scegli una opzione:" + "\n" + "1:Nuova scoperta" + "\n" + "2: Risultati in archivio", vbox_messages, "-fx-background-color: rgb(36,133,29);");
                                    else{
                                        if(!value.isEmpty()){
                                            opzione = number;
                                            client.sendMessageToServer(String.valueOf(opzione));
                                        }
                                        value = new String();
                                    }
                                }catch(NumberFormatException ex){
                                    ClientController.addLableLeftMessage("Scegli una opzione:" + "\n" + "1:Nuova scoperta" + "\n" + "2: Risultati in archivio", vbox_messages, "-fx-background-color: rgb(36,133,29);");}

                            }if(opzione != 0 && minSup == 0f && !clientError){
                                    if(value.isEmpty())
                                    ClientController.addLableLeftMessage("Inserire valore minimo supporto (minsup>0 e minsup<=1):", vbox_messages, "-fx-background-color: rgb(36,133,29);");
                                    if(!value.isEmpty()) {
                                        try {
                                            float number = Float.parseFloat(value);
                                            if (number <= 0 || number > 1) {
                                                ClientController.addLableLeftMessage("Inserire valore minimo supporto (minsup>0 e minsup<=1):", vbox_messages, "-fx-background-color: rgb(36,133,29);");
                                            } else {
                                                minSup = number;
                                                client.sendMessageToServer(String.valueOf(minSup));
                                                value = new String();
                                            }
                                        }catch(NumberFormatException ex){
                                            minSup = Float.NaN;
                                            client.sendMessageToServer(String.valueOf(minSup));
                                            value = new String();
                                            Platform.runLater(() -> {
                                                Alert dialog = new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK);
                                                dialog.setHeaderText("Inserito minSup di tipo diverso dal Float");
                                                dialog.setContentText("Non hai inserito in minSup che sia un Float. Di conseguenza gli verrà assegnato un valore pari a Nan di default");
                                                dialog.show();
                                            });
                                        }
                                    }
                            }
                            if(opzione != 0 && minSup != 0f && minGr == 0f  && !clientError){
                                if(value.isEmpty())
                                    ClientController.addLableLeftMessage("Inserire valore minimo grow rate (minGr>0):", vbox_messages, "-fx-background-color: rgb(36,133,29);");
                                if(!value.isEmpty()){
                                    try {
                                        float number = Float.parseFloat(value);
                                        if (number <= 0) {
                                            ClientController.addLableLeftMessage("Inserire valore minimo grow rate (minGr>0):", vbox_messages, "-fx-background-color: rgb(36,133,29);");
                                        } else {
                                            minGr = number;
                                            client.sendMessageToServer(String.valueOf(minGr));
                                            value = new String();
                                        }
                                    }catch(NumberFormatException ex) {
                                        minGr = Float.NaN;
                                        client.sendMessageToServer(String.valueOf(minGr));
                                        value = new String();


                                        Platform.runLater(() -> {
                                            Alert dialog = new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK);
                                            dialog.setHeaderText("Hai inserito una stringa come minGr di tipo diverso dal Float");
                                            dialog.setContentText("Non hai inserito in minGr che sia un Float. Di conseguenza gli verrà assegnato un valore pari a Nan di default");
                                            dialog.show();
                                        });
                                    }
                                }
                            }
                            if(opzione != 0 && minSup != 0f && minGr != 0f && targetTable.isEmpty() && !clientError){
                                if(value.isEmpty())
                                    ClientController.addLableLeftMessage("Tabella target:", vbox_messages, "-fx-background-color: rgb(36,133,29);");
                                if(!value.isEmpty()){
                                    targetTable = value;
                                    client.sendMessageToServer(targetTable);
                                    try {
                                        ClientController.addLabelServerMessage(client.receiveMessageToServer(), vbox_messages, "-fx-background-color: rgb(233,233,235);");
                                    }catch(IOException | ClassNotFoundException ex){
                                        ClientController.addLableLeftMessage("Il server sembra non rispondere...", vbox_messages, "-fx-background-color: rgb(255,0,0);");
                                    }
                                    value = new String();
                                }
                            }
                        if(opzione != 0 && minSup != 0f && minGr != 0f && !targetTable.isEmpty() && backgroundTable.isEmpty() && !clientError){
                            if(value.isEmpty())
                                ClientController.addLableLeftMessage("Tabella background:", vbox_messages, "-fx-background-color: rgb(36,133,29);");
                            if(!value.isEmpty()){
                                backgroundTable = value;
                                client.sendMessageToServer(backgroundTable);
                                try {
                                    ClientController.addLabelServerMessage(client.receiveMessageToServer(), vbox_messages, "-fx-background-color: rgb(233,233,235);");
                                }catch(IOException | ClassNotFoundException ex){
                                    ClientController.addLableLeftMessage("Il server sembra non rispondere...", vbox_messages, "-fx-background-color: rgb(255,0,0);");
                                }
                                value = new String();
                            }
                        }
                        if(opzione != 0 && minSup !=0f && minGr != 0f && !targetTable.isEmpty() && !backgroundTable.isEmpty() && risp.isEmpty() || clientError){
                            if(!value.trim().equals("s") && !value.trim().equals("n") || !risp.trim().equals("n") && !risp.trim().equals("s"))
                            ClientController.addLableLeftMessage("Vuoi chiudere la chat?(s/n)", vbox_messages, "-fx-background-color: rgb(36,133,29);");
                            if(!value.isEmpty() && value.trim().equals("s") || value.trim().equals("n")){
                                risp = value;
                                value = new String();
                            }
                        }
                        if(opzione != 0 && minSup != 0f && minGr != 0f && !targetTable.isEmpty() && !backgroundTable.isEmpty() && !risp.isEmpty() || clientError){
                            if(risp.trim().equals("s")){
                                System.exit(0);
                                try {
                                    if(!clientError)
                                    client.closeSocket();
                                }catch(IOException | InterruptedException ex){ex.fillInStackTrace(); System.err.println("Errore durante la chisura del Client..");}
                            }
                            else if(risp.trim().equals("n") && clientError){
                                ClientController.addLableLeftMessage("Nessun server in ascolto all'indirizzo " + address + " Porta: " + port,vbox_messages,"-fx-background-color: rgb(255,0,0);");
                                ClientController.addLableLeftMessage("Riprovare...",vbox_messages,"-fx-background-color: rgb(255,0,0);");
                                risp = new String();
                            }
                            else if (risp.trim().equals("n")){
                                opzione = 0;
                                minGr = 0f;
                                minSup = 0f;
                                targetTable = new String();
                                backgroundTable = new String();
                                risp = new String();
                                addLableLeftMessage("Scegli una opzione:" + "\n" + "1:Nuova scoperta" + "\n" + "2: Risultati in archivio", vbox_messages, "-fx-background-color: rgb(36,133,29);");
                            } else risp = new String();

                        }

                        }
                        tf_message.clear();
                    }
                });
    }

    public static void addLableLeftMessage(String messageFromClient, VBox vBox,String backgroundColor){

        HBox hBox1 = new HBox();
        hBox1.setAlignment(Pos.TOP_LEFT);
        hBox1.setPadding(new Insets(5,5,1,10));
        Text time = new Text(getCurrentTime());
        time.setFill(Color.BLACK);
        time.setStyle("-fx-font: 10 arial;");
        TextFlow timeFlow = new TextFlow(time);
        hBox1.getChildren().add(timeFlow);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(1,5,5,10));
        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle(backgroundColor + "-fx-background-radius:20px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);
        Image img = new Image("Images/system.jpg");
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(30);
        imgView.setFitWidth(50);
        Circle circle = new Circle(0, 0, 30);
        ImagePattern pattern = new ImagePattern(new Image("Images/system.jpg", 280, 180, false, false));
        circle.setFill(pattern);
        circle.setEffect(new InnerShadow(10, Color.BLACK));  // Shadow
        hBox.getChildren().add(imgView);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox1);
                vBox.getChildren().add(hBox);
            }
        });
    }

    public static void addLabelServerMessage(String messageFromServer, VBox vBox,String backgroundColor){
        HBox hBox1 = new HBox();
        hBox1.setAlignment(Pos.TOP_LEFT);
        hBox1.setPadding(new Insets(5,5,1,10));
        Text time = new Text(getCurrentTime());
        time.setFill(Color.BLACK);
        time.setStyle("-fx-font: 10 arial;");
        TextFlow timeFlow = new TextFlow(time);
        hBox1.getChildren().add(timeFlow);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(1,5,5,10));
        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle(backgroundColor + "-fx-background-radius:20px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);
        Image img = new Image("Images/server.png");
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(30);
        imgView.setFitWidth(50);
        Circle circle = new Circle(0, 0, 30);
        ImagePattern pattern = new ImagePattern(new Image("Images/server.png", 280, 180, false, false));
        circle.setFill(pattern);
        circle.setEffect(new InnerShadow(10, Color.BLACK));  // Shadow
        hBox.getChildren().add(imgView);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox1);
                vBox.getChildren().add(hBox);
            }
        });
    }

    static void setLoginProperty(String username1, String andressIp1, int port1){
        username = username1;
        address = andressIp1;
        port = port1;
    }

    static void setImagePath(String image){
        imagePath = image;
    }

    static private String getCurrentTime(){
        String timeStamp = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        return timeStamp;
    }

    }

