package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import pl.polsl.bos.ann.*;
import pl.polsl.bos.ann.neurons.INeuron;

import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 450, 300));
        primaryStage.show();

        NetManager netM = new NetManager(4,32,16);
        netM.performTeaching();
    }


    public static void main(String[] args) {
        launch(args);


    }
}
