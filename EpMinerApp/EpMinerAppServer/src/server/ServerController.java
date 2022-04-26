package server;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    @FXML
    private VBox vbox_messages;
    @FXML
    private ScrollPane sp_main;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            try {
                MultiServer server = new MultiServer(vbox_messages);
            } catch (IOException exception) {
                exception.fillInStackTrace();
                System.out.println("Error creating Server...");
            }
        }).start();

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                sp_main.setVvalue((Double ) t1);
            }
        });

    }

    public static void addLabelClientMessage(String messageFromClient, VBox vBox,String backgroundColor,String clientImagePath,String username){
        HBox hBox1 = new HBox();
        hBox1.setAlignment(Pos.TOP_LEFT);
        hBox1.setPadding(new Insets(5,5,1,10));
        Text time = new Text(getCurrentTime());
        time.setFill(Color.BLACK);
        time.setStyle("-fx-font: 10 arial;");
        TextFlow timeFlow = new TextFlow(time);
        hBox1.getChildren().add(timeFlow);

        Text usernameText = new Text(username);
        usernameText.setFill(Color.BLACK);
        usernameText.setStyle("-fx-font: 10 arial;");
        TextFlow flowUsername = new TextFlow(usernameText);
        flowUsername.setPadding(new Insets(5,10,5,10));

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(1,5,5,10));
        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle(backgroundColor + "-fx-background-radius:20px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);
        Image img = new Image(clientImagePath);
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(30);
        imgView.setFitWidth(30);
        Circle circle = new Circle(0, 0, 30);
        ImagePattern pattern = new ImagePattern(new Image(clientImagePath, 280, 180, false, false));
        circle.setFill(pattern);
        circle.setEffect(new InnerShadow(10, Color.BLACK));  // Shadow
        hBox.getChildren().add(imgView);
        hBox.getChildren().add(flowUsername);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox1);
                vBox.getChildren().add(hBox);
            }
        });
    }

    public static void addLabelSystemMessage(String messageFromClient, VBox vBox,String backgroundColor){
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


    public static void addLableRightMessage(String messageToSend, VBox vBox){
        HBox hBox1 = new HBox();
        hBox1.setAlignment(Pos.TOP_RIGHT);
        hBox1.setPadding(new Insets(5,5,1,10));
        Text time = new Text(getCurrentTime());
        time.setFill(Color.BLACK);
        time.setStyle("-fx-font: 10 arial;");
        TextFlow timeFlow = new TextFlow(time);
        hBox1.getChildren().add(timeFlow);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(1,5,5,10));
        Text text = new Text(messageToSend);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color:rgb(239,242,255);" + "-fx-background-color: rgb(15,12,242);" + "-fx-background-radius:20px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        text.setFill(Color.color(0.934, 0.945, 0.996));
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

    static private String getCurrentTime(){
        String timeStamp = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        return timeStamp;
    }
}
