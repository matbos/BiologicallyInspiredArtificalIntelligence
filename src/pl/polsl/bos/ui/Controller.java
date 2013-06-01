/**
 * Sample Skeleton for "sample.fxml" Controller Class
 * You can copy and paste this code into your favorite IDE
 **/

package pl.polsl.bos.ui;

import com.tomgibara.CannyEdgeDetector;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import pl.polsl.bos.ann.NetManager;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller
        implements Initializable, Progressable {

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
    private TextField lowThTextField;
    @FXML
    private TextField highThTextField;
    @FXML
    private TextField maxErrorTextField;
    @FXML
    private TextField iterationsTextField;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private LineChart lineChart;
    @FXML
    private TextField currentErrorTextField;

    private BufferedImage originalBufferedImage = null;

    private NetManager networkManager = null;

    private int maxIterations = 0;

    private LineChart.Series series;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert edgedImage != null : "fx:id=\"edgedImage\" was not injected: check your FXML file 'sample.fxml'.";
        assert learningTab != null : "fx:id=\"learningTab\" was not injected: check your FXML file 'sample.fxml'.";
        assert originalImage != null : "fx:id=\"originalImage\" was not injected: check your FXML file 'sample.fxml'.";
        assert recognitionTab != null : "fx:id=\"recognitionTab\" was not injected: check your FXML file 'sample.fxml'.";
        assert numberOfHiddenNeuronTextField != null : "fx:id=\"numberOfHiddenNeuronTextField\" was not injected: check your FXML file 'sample.fxml'.";
        assert lowThTextField != null : "fx:id=\"lowThTextView \" was not injected: check your FXML file 'sample.fxml'.";
        assert highThTextField != null : "fx:id=\"highThTextView \" was not injected: check your FXML file 'sample.fxml'.";
        assert maxErrorTextField != null : "fx:id=\"maxErrorTextField \" was not injected: check your FXML file 'sample.fxml'.";
        assert iterationsTextField != null : "fx:id=\"iterationsTextField\" was not injected: check your FXML file 'sample.fxml'.";
        assert progressBar != null : "fx:id=\"progressBar\" was not injected: check your FXML file 'sample.fxml'.";
        assert lineChart != null : "fx:id=\"lineChart\" was not injected: check your FXML file 'sample.fxml'.";
        assert currentErrorTextField != null : "fx:id=\"currentErrorTextField\" was not injected: check your FXML file 'sample.fxml'.";


        // initialize your logic here: all @FXML variables will have been injected

    }

    public void openFileToRecognize(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("BMP Images (*.bmp)", "*.bmp");
        //fileChooser.getExtensionFilters().add(extFilter);

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
        /*
        // Algorytm FFT, nie działa do końca
        Image img = originalImage.getImage();
        PixelReader ipr = originalImage.getImage().getPixelReader();
        double redTT[][] = new double[200][400];
        double greenTT[][] = new double[200][400];
        double blueTT[][] = new double[200][400];
        DoubleFFT_2D fft = new DoubleFFT_2D(200, 200);
        int transform[] = new int[200 * 200];

        for (int i = 0; i < img.getWidth(); ++i)
            for (int j = 0; j < img.getHeight(); ++j) {
                redTT[i][j] = ipr.getColor(i, j).getRed();
                greenTT[i][j] = ipr.getColor(i, j).getGreen();
                greenTT[i][j] = ipr.getColor(i, j).getBlue();
            }

        fft.realForwardFull(redTT);
        fft.realForwardFull(blueTT);
        fft.realForwardFull(greenTT);
        //TODO: Zrobić z tego obraz, wrzucić do edgedImage i umożliwić zapis do pliku.
        //
        // save transform to image!
        for (int j = 0; j < img.getWidth(); ++j)
            for (int i = 0; i < img.getHeight(); ++i) {
                transform[j * 200 + i] = ((byte) Double.doubleToLongBits(redTT[j][200 + i])) << 24 + ((byte) Double.doubleToLongBits(greenTT[j][200 + i])) << 16 + ((byte) Double.doubleToLongBits(blueTT[j][200 + i])) << 8;
            }


        BufferedImage bim = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        bim.setRGB(0, 0, 200, 200, transform, 0, 200);
        WritableImage writableImage = new WritableImage(bim.getWidth(), bim.getHeight());
        SwingFXUtils.toFXImage(bim, writableImage);
        edgedImage.setImage(writableImage);
        File file = new File("image1234.bmp");
        try {
            ImageIO.write(bim, "bmp", file);
        } catch (IOException e) {
            System.out.write(123);
        }
    }

                    */
        CannyEdgeDetector edgeDetector = new CannyEdgeDetector();
//adjust its parameters as desired
        //Float low = Float.valueOf(lowT)
        edgeDetector.setLowThreshold(Float.valueOf(lowThTextField.getText()));
        edgeDetector.setHighThreshold(Float.valueOf(highThTextField.getText()));

//apply it to an image
        edgeDetector.setSourceImage(originalBufferedImage);
        edgeDetector.process();
        BufferedImage edges = edgeDetector.getEdgesImage();
        WritableImage writableImage = new WritableImage(edges.getWidth(), edges.getHeight());
        SwingFXUtils.toFXImage(edges, writableImage);
        edgedImage.setImage(writableImage);
        String str = networkManager.recognizeImage(edges);
        System.out.println("THIS IS "+str);

    }

    public void teachTheNetworkOnClick(MouseEvent event){
        float lowTH  = Float.valueOf(lowThTextField.getText());
        float highTH = Float.valueOf(highThTextField.getText());
        double error = Double.valueOf(maxErrorTextField.getText());
        File directory = pickDirectory(numberOfHiddenNeuronTextField.getScene().getWindow());
        if(directory == null)
            return;
        maxIterations = Integer.valueOf(iterationsTextField.getText());
        int outputCount = NetManager.countDirectories(directory.listFiles());
        networkManager = new NetManager(1600,Integer.valueOf(numberOfHiddenNeuronTextField.getText()),outputCount);
        networkManager.performTeaching(lowTH,highTH,directory,error,maxIterations,this);
        progressBar.setProgress(1.0);
    }


    private File pickDirectory(Window window){
        DirectoryChooser d = new DirectoryChooser();
        File directory = d.showDialog(window);
        return directory;
    }
    @Override
    public void giveFeedback(int iteration, double error) {
        progressBar.setProgress(iteration/maxIterations);
        currentErrorTextField.setText(Double.valueOf(error).toString());
        //populating the series with data
        //series.getData().add(new LineChart.Data(iteration, error));
        //lineChart.getData().add(series);
    }

}
