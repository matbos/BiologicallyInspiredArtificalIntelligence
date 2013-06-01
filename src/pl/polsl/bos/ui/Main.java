package pl.polsl.bos.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import pl.polsl.bos.ann.*;

public class Main extends Application {

    NetManager networkManager = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent page = FXMLLoader.load(getClass().getResource("sample.fxml"), null, new JavaFXBuilderFactory());
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            scene = new Scene(page, 700, 700, Color.web("#9F9F9F",0.5));
            primaryStage.setScene(scene);
        } else {
            primaryStage.getScene().setRoot(page);
        }
        primaryStage.sizeToScene();
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);


    }
}
