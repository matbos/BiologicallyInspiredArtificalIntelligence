package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import pl.polsl.bos.ann.InputManager;

import javafx.scene.image.ImageView;
import sun.security.util.Resources;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    ImageView trI = null;
/*
    @FXML protected void originalDropped(DragEvent event) {
        // data dropped
        System.out.println("onDragDropped");
                // if there is a string data on dragboard, read it and use it
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
          //  target.setText(db.getString());
            success = true;
        }
                // let the source know whether the string was successfully
                // transferred and used
        event.setDropCompleted(success);

        event.consume();

    }
    @FXML protected void buttonInit(ActionEvent actionEvent) {
        InputManager im = new InputManager();
        im.recognizeImageBMP("img.bmp",trI);
    }
  */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // trI = (ImageView)resourceBundle.getObject("transformI");
    }
}
