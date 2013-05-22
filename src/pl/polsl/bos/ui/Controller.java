/**
 * Sample Skeleton for "sample.fxml" Controller Class
 * You can copy and paste this code into your favorite IDE
 **/

package pl.polsl.bos.ui;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_2D;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert edgedImage != null : "fx:id=\"edgedImage\" was not injected: check your FXML file 'sample.fxml'.";
        assert learningTab != null : "fx:id=\"learningTab\" was not injected: check your FXML file 'sample.fxml'.";
        assert originalImage != null : "fx:id=\"originalImage\" was not injected: check your FXML file 'sample.fxml'.";
        assert recognitionTab != null : "fx:id=\"recognitionTab\" was not injected: check your FXML file 'sample.fxml'.";

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
                BufferedImage bufferedImage = ImageIO.read(file);
                WritableImage writableImage = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
                SwingFXUtils.toFXImage(bufferedImage, writableImage);
                originalImage.setImage(writableImage);

            } catch (IOException e) {
                System.out.println(e.getStackTrace());  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }

    public void processFileToRecognize(MouseEvent event) {
        Image img = originalImage.getImage();
        PixelReader ipr = originalImage.getImage().getPixelReader();
        double redTT[][] = new double[200][400];
        double greenTT[][] = new double[200][400];
        double blueTT[][] = new double[200][400];
        DoubleFFT_2D fft = new DoubleFFT_2D(200, 200);
        int transform[] = new int[200*200];

        for (int i = 0; i < img.getWidth(); ++i)
            for (int j = 0; j < img.getHeight(); ++j) {
                redTT[i][j]   = ipr.getColor(i, j).getRed();
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
                transform[i * 200 + j] = ((byte)Double.doubleToLongBits(redTT[j][200 + i])) << 24 + ((byte)Double.doubleToLongBits(greenTT[j][200 + i])) << 16 + ((byte)Double.doubleToLongBits(blueTT[j][200 + i])) << 8;
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




}
