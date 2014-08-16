package arad.amir.ac.snlje.windowed;

import arad.amir.ac.snlje.windowed.controllers.ControllersManager;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application extends javafx.application.Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    @Override
    public void start(Stage primaryStage) throws Exception{
        ControllersManager manager = new ControllersManager(primaryStage);
        primaryStage.setTitle("Snakes and Ladders - Amir Arad");
        primaryStage.setScene(new Scene(manager.fxmlLoad("PrimaryStage.fxml")));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
