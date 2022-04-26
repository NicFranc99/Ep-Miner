import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        Image img = new Image("/Images/server3.png");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("resources/mainController.fxml"));
            primaryStage.getIcons().add(img);
            primaryStage.setScene(new Scene(root,400,650));
            primaryStage.setTitle("Server");
            primaryStage.setResizable(false);
            primaryStage.show();
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