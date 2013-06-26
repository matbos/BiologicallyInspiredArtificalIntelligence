/**
 * Sample Skeleton for "sample.fxml" Controller Class
 * You can copy and paste this code into your favorite IDE
 **/

package pl.polsl.bos.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import pl.polsl.bos.ann.Mapping;
import pl.polsl.bos.ann.NetManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;


public class Controller
        implements Initializable {

    @FXML //  fx:id="edgedImage"
    private ImageView edgedImage; // Value injected by FXMLLoader
    @FXML //  fx:id="learningTab"
    private Tab learningTab; // Value injected by FXMLLoader
    @FXML //  fx:id="originalImage"
    private ImageView originalImage; // Value injected by FXMLLoader
    @FXML //  fx:id="recognitionTab"
    private Tab recognitionTab; // Value injected by FXMLLoader
    @FXML
    private TextField numberOfHiddenNeuronTextField; // Value injected by FXMLLoader
    @FXML
    private TextField maxErrorTextField;
    @FXML
    private TextField iterationsTextField;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private TableView learningErrorsTable;
    @FXML
    private TableView outputErrorsTable;
    @FXML
    private Label resultText;

    private BufferedImage originalBufferedImage = null;

    private NetManager networkManager = null;

    private int maxIterations = 0;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert edgedImage != null : "fx:id=\"edgedImage\" was not injected: check your FXML file 'sample.fxml'.";
        assert learningTab != null : "fx:id=\"learningTab\" was not injected: check your FXML file 'sample.fxml'.";
        assert originalImage != null : "fx:id=\"originalImage\" was not injected: check your FXML file 'sample.fxml'.";
        assert recognitionTab != null : "fx:id=\"recognitionTab\" was not injected: check your FXML file 'sample.fxml'.";
        assert numberOfHiddenNeuronTextField != null : "fx:id=\"numberOfHiddenNeuronTextField\" was not injected: check your FXML file 'sample.fxml'.";
        assert maxErrorTextField != null : "fx:id=\"maxErrorTextField \" was not injected: check your FXML file 'sample.fxml'.";
        assert iterationsTextField != null : "fx:id=\"iterationsTextField\" was not injected: check your FXML file 'sample.fxml'.";
        assert progressBar != null : "fx:id=\"progressBar\" was not injected: check your FXML file 'sample.fxml'.";
        assert learningErrorsTable != null :  "fx:id=\"learningErrorsTable\" was not injected: check your FXML file 'sample.fxml'.";
        assert outputErrorsTable != null :  "fx:id=\"outputErrorsTable\" was not injected: check your FXML file 'sample.fxml'.";
        assert resultText != null :  "fx:id=\"resultText\" was not injected: check your FXML file 'sample.fxml'.";
        // initialize your logic here: all @FXML variables will have been injected
    }

    public void openFileToRecognize(MouseEvent event) {
        resultText.setText("");
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("BMP Images (*.bmp)", "*.bmp", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                originalBufferedImage = ImageIO.read(file);
                WritableImage writableImage = new WritableImage(originalBufferedImage.getWidth(), originalBufferedImage.getHeight());
                SwingFXUtils.toFXImage(originalBufferedImage, writableImage);
                originalImage.setImage(writableImage);
            } catch (IOException e) {
                System.out.println(e.getStackTrace());  //To change body of catch statement use File | Settings | File Templates.
            }
        }


    }

    public void processFileToRecognize(MouseEvent event) {
        //apply it to an image
        WritableImage writableImage = new WritableImage(originalBufferedImage.getWidth(), originalBufferedImage.getHeight());
        SwingFXUtils.toFXImage(originalBufferedImage,writableImage);
        edgedImage.setImage(writableImage);
        String str = networkManager.recognizeImage(originalBufferedImage);
        final ObservableList<Mapping> outs = FXCollections.observableArrayList();
        for(Mapping m : networkManager.getMakes()){
            outs.add(m);
        }
        resultText.setText("I believe this is " + str);
        TableColumn make = new TableColumn("Make");
        TableColumn error = new TableColumn("Certainty");
        make.setCellValueFactory(new PropertyValueFactory<Mapping, String>("name"));
        error.setCellValueFactory(new PropertyValueFactory<Mapping, Double>("error"));
        outputErrorsTable.getColumns().remove(0);
        outputErrorsTable.getColumns().remove(0);
        outputErrorsTable.getColumns().addAll(make, error);
        outputErrorsTable.setItems(outs);

    }

    public void teachTheNetworkOnClick(MouseEvent event){
        double error = Double.valueOf(maxErrorTextField.getText());
        File directory = pickDirectory(numberOfHiddenNeuronTextField.getScene().getWindow());
        if(directory == null)
            return;
        maxIterations = Integer.valueOf(iterationsTextField.getText());
        int outputCount = NetManager.countDirectories(directory.listFiles());
        networkManager = new NetManager(1600,Integer.valueOf(numberOfHiddenNeuronTextField.getText()),outputCount+1);
        networkManager.performTeaching(directory, error, maxIterations, this);
        progressBar.setProgress(1.0);

        TableColumn hiddenTableColumn = new TableColumn("Hidden");
        TableColumn outputTableColumn = new TableColumn("Output");
        final ObservableList<LearningSet> lista = FXCollections.observableArrayList();

        for(double d : networkManager.getHiddenErrors()){
            lista.add(new LearningSet(d));
        }
        Iterator<LearningSet> iter = lista.iterator();

        for(double d : networkManager.getOutputErrors()) {
            if(iter.hasNext()) {
                LearningSet set = iter.next();
                set.setOutput(d);
            } else
                break;
        }

        hiddenTableColumn.setCellValueFactory(new PropertyValueFactory<LearningSet, Double>("hidden"));
        outputTableColumn.setCellValueFactory(new PropertyValueFactory<LearningSet, Double>("output"));
        learningErrorsTable.getColumns().remove(0);
        learningErrorsTable.getColumns().remove(0);
        learningErrorsTable.getColumns().addAll(hiddenTableColumn, outputTableColumn);
        learningErrorsTable.setItems(lista);

    }

    private File pickDirectory(Window window){
        DirectoryChooser d = new DirectoryChooser();
        File directory = d.showDialog(window);
        return directory;
    }

    public void giveFeedback(int iteration, double error) {
        progressBar.setProgress(iteration/maxIterations);
    }
}
