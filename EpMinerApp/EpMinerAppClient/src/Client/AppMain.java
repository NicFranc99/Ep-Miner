package Client;

import Client.ClientController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Pair;
import java.io.File;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;


public class AppMain extends Application {
    private Label labelAddress;
    private Label labelPort;

    //Button
    private Button buttonImg;

    //Text
    private TextField textAddress;
    private TextField textPort;

    //ChoiceBox
    private ChoiceBox<Object> textChoice;

    //Image
    private Image img;
    private ImageView imgView;
    private ImagePattern pattern;
    private Circle circle;
    //Locale
    private  Locale enLocale;
    private Locale itLocale;
    private Locale currentLocale;

    //ResourceBundle
    private ResourceBundle messages;
    @Override
    public void start(Stage primaryStage) {
            enLocale = new Locale("en","EN");
            itLocale = new Locale("it","IT");

            currentLocale = new Locale(new String(), new String());

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Language Selection");
            alert.setHeaderText("Select a language, please");
            alert.setContentText("Choose your option.");

            ButtonType buttonTypeOne = new ButtonType("Italiano/Italian");
            ButtonType buttonTypeTwo = new ButtonType("Inglese/English");
            ButtonType buttonTypeCancel = new ButtonType("Esci/Exit", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

            Optional<ButtonType> result2 = alert.showAndWait();
            if(result2.get() == buttonTypeOne)
                currentLocale = itLocale;
             else if (result2.get() == buttonTypeTwo)
                currentLocale = enLocale;
                else
                    System.exit(0);
            messages = ResourceBundle.getBundle("Properties.MessagesBundle", currentLocale);

            //Labels
            labelAddress = new Label(messages.getString("address"));
            labelPort = new Label(messages.getString("port"));

            //Text
            textAddress = new TextField();
            textPort = new TextField();
            textChoice = new ChoiceBox<Object>(FXCollections.observableArrayList(messages.getString("first"), new Separator(), messages.getString("second")));

            //Button
            buttonImg = new Button(messages.getString("img"));
            buttonImg.setId("button");

            Group root = new Group();
            Scene scene = new Scene(root, 600, 250, Color.WHITE);
            //scene.getStylesheets().add("CSS/AppClient.css");

            TabPane tabPane = new TabPane();
            tabPane.setSide(Side.TOP);
            tabPane.setId("tabPane");

            BorderPane borderPane = new BorderPane();
            borderPane.setId("borderPane");
            img = new Image("Images/user.png");
            imgView = new ImageView(img);
            imgView.setFitHeight(100);
            imgView.setFitWidth(100);
            imgView.setTranslateX(325);
            imgView.setTranslateY(95);
            if(result2.get() == buttonTypeTwo) {
                imgView.setTranslateX(260);
                imgView.setTranslateY(95);
            }
            imgView.setId("image");
            circle = new Circle(0, 0, 40);
            pattern = new ImagePattern(new Image("Images/user.png", 280, 180, false, false));
            circle.setFill(pattern);
            circle.setEffect(new InnerShadow(10, Color.BLACK));  // Shadow

            //VBox
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.TOP_CENTER);
            vbox.setTranslateX(7);
            vbox.setTranslateY(20);
            vbox.setPadding(new Insets(5, 5, 5, 5));
            vbox.setSpacing(20);
            borderPane.setTop(vbox);

            ImageView imgView1 = new ImageView("Images/user.png");
            imgView1.setFitWidth(20);
            imgView1.setFitHeight(20);
            ImageView imgView2 = new ImageView("Images/user_female.png");
            imgView2.setFitWidth(20);
            imgView2.setFitHeight(20);
            ImageView imgView3 = new ImageView("Images/icona-omino-png-1.png");
            imgView3.setFitWidth(20);
            imgView3.setFitHeight(20);
            Menu fileMenu = new Menu(messages.getString("select_immage"));
            //Creating menu Items
            MenuItem item1 = new MenuItem("User image1", imgView1);
            MenuItem item2 = new MenuItem("User image2", imgView2);
            MenuItem item3 = new MenuItem("User image3", imgView3);
            //Adding all the menu items to the menu
            fileMenu.getItems().addAll(item1, item2, item3);
            //Creating a menu bar and adding menu to it.
            MenuBar menuBar = new MenuBar(fileMenu);
            menuBar.setTranslateX(10);
            menuBar.setTranslateY(10);
            menuBar.setId("menu");
            item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                imgView.setImage(new Image("Images/user.png"));
            }
        });
        item2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                imgView.setImage(new Image("Images/user_female.png"));
            }
        });
        item3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                imgView.setImage(new Image("Images/icona-omino-png-1.png"));
            }
        });

            //buttonImg
            buttonImg.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("PNG Files", "*.png")
                            ,new FileChooser.ExtensionFilter("JPEG Files", "*.jpeg")
                    );

                    fileChooser.setTitle("Open Resource File");
                    File selectedFile = fileChooser.showOpenDialog(primaryStage);
                    if(selectedFile!=null) {
                        Image image = new Image(selectedFile.toURI().toString());
                        imgView.setImage(new Image(image.getUrl()));
                        ImagePattern pattern = new ImagePattern(image);
                        circle.setFill(pattern);
                    }
                }
            });

            ///INIZIO DEFINIZIONE FINESTRA DI LOGIN///

            // Crea il dialog personalizzato
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(imgView.getImage());
            dialog.setTitle("EPMiner Client Login");
            dialog.setHeaderText(messages.getString("login_header"));
            dialog.getDialogPane().getStylesheets().add(
            getClass().getResource("/CSS/LoginDialog.css").toExternalForm());
            dialog.getDialogPane().getStyleClass().add("error");
            // Inizializza il tipo dei bottoni
            ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType(messages.getString("exit"), ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);
            dialog.getDialogPane().getChildren().add(imgView);
            // Crea i labels e i fields relativi all'username , password , IpAddress e Port
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            grid.setId("borderPane");

            TextField username = new TextField();
            username.setPromptText("Username");
            username.setId("username-field");
            PasswordField password = new PasswordField();
            password.setPromptText("Password");

            TextField addressIp = new TextField();
        addressIp.setPromptText(messages.getString("address"));
        addressIp.setId("addressIp-field");

        ObservableList<String> options =
                FXCollections.observableArrayList("8080", "8081", "8082");
        final ComboBox comboBox = new ComboBox(options);

        comboBox.setValue(options.get(0));
            grid.add(new Label("Username:"), 0, 0);
            grid.add(username, 1, 0);
            grid.add(new Label("Password:"), 0, 1);
            grid.add(password, 1, 1);
            grid.add(new Label(messages.getString("address")+":"), 0, 2);
            grid.add(addressIp, 1, 2);
            grid.add(new Label(messages.getString("port")+""), 0, 3);
            grid.add(comboBox, 1, 3);
            grid.add(buttonImg,1,4);
            grid.add(menuBar,1,5);

            // Attiva/Disattiva il bottone di Login a seconda dell'inserimento di un AndressIp e Username
            Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);

        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(addressIp.textProperty(),
                        username.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (addressIp.getText().trim().isEmpty()
                        || username.getText().trim().isEmpty());
            }
        };
        loginButton.disableProperty().bind(bb);
        dialog.getDialogPane().setContent(grid);


        username.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.trim().isEmpty())
                username.setStyle(" -fx-border-color: green;");
            else
                username.setStyle(" -fx-border-color: red;");
        });

        addressIp.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.trim().isEmpty())
                addressIp.setStyle(" -fx-border-color: green;");
            else
                addressIp.setStyle(" -fx-border-color: red;");
        });


            // Richiede di default il focus sul field dell'username
            Platform.runLater(() -> username.requestFocus());

            //Converte il risultato in una coppia adnressIp-port quando il bottone di Login viene cliccato
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType) {
                    return new Pair<>(addressIp.getText(), comboBox.getValue().toString());
                }
                return null;
            });
            Optional<Pair<String, String>> result = dialog.showAndWait();
            if(!result.isPresent()) {
                System.exit(0);
            }else {
                    ClientController.setLoginProperty(username.getText().trim(), addressIp.getText().trim(), Integer.parseInt(comboBox.getValue().toString()));
                    ClientController.setImagePath(imgView.getImage().getUrl());
                    primaryStage.show();
            }
            ///FINE DEFINIZIONE FINESTRA DI LOGIN///

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Resources/clientController.fxml"));
            Parent root1 = (Parent)fxmlLoader.load();
            ClientController clientController = fxmlLoader.<ClientController>getController();
            Scene scene1 = new Scene(root1,400,650);
            primaryStage.setScene(scene1);
            primaryStage.getIcons().add(imgView.getImage());
            primaryStage.setTitle("Client");
            primaryStage.show();
            primaryStage.setResizable(false);

        } catch(Exception e) {
            e.printStackTrace();
        }

        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}