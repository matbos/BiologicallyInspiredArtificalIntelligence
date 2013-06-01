package pl.polsl.bos.ann;

import edu.emory.mathcs.jtransforms.fft.FloatFFT_2D;
import pl.polsl.bos.ann.exceptions.NoConnectionBetweenThoseNeuronsException;
import pl.polsl.bos.ann.neurons.INeuron;
import pl.polsl.bos.ann.neurons.InputNeuron;

import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.management.BadAttributeValueExpException;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz Boś
 * Date: 14.05.13
 * Time: 23:37
 */
public class InputManager {

    private ArrayList<INeuron> inputNeurons;
    private BufferedImage image = null;

    public InputManager(ArrayList<INeuron> input){
        inputNeurons = input;
    }
    public InputManager(){

    }
    public void setInputNeurons(ArrayList<INeuron> list ){
        this.inputNeurons = list;
    }
    /**
     * Method does not return the outcome, in order to get one
     * invoke getResult() method.
     * @param pathToImage
     */
    public void feedImage(String pathToImage){
      //  double[][] tileValues = new double[40][40];
      //  float[][] redTT = new float[400][400];
      //  float[][] blueTT = new float[400][400];
      //  float[][] greenTT = new float[400][400];
      //  int[] transform = new int[40000];
        if (inputNeurons == null)
            //TODO: napisac osobny wyjatek z tej sytuacji!!!!
            throw new NoConnectionBetweenThoseNeuronsException();

        BufferedImage img = null;
        try{
            img = ImageIO.read(new File(pathToImage));
        } catch (IOException e) {
            // TODO: error loading image, some communicate ?!
            System.out.println(e.getStackTrace());
        }
        if(img.getWidth() != 200 || img.getWidth() != img.getHeight() )
        {
            // TODO: niepoprawny plik został załadowany, poprawny rozmiar to 200x200 px, obsłużyć to
            return;
        }

        feedNetwork();
    }
    public void feedImage(BufferedImage img){
        if (img == null)
            throw new RuntimeException("image was null, not nice!");
        image = img;
        feedNetwork();
    }

    private double[] sampleImage(){
        // podzielić obraz na części 5x5 px
        // jeśli w obrazie wystąpi pixel o kolorze ff8080
        // to jego wartość to suma pixeli
        // narazie na sztywno ale trzeba to będzie poprawić
        //TODO: Dynamiczny podział obrazka na części
        double values[] = new double[1600];
        for(int counter = 0,  i=0; i<200; i+=5){
            for(int j = 0; j<200;j+=5){
               values[counter++] = sumPixels(i,j);
            }
        }
        return values;
    }

    private double sumPixels(int x, int y){
        double value = -2D;
        for(int i=0; i<5; ++i){
            for(int j = 0; j<5;++j){
                Color color = new Color(image.getRGB(x+i,y+j));
                if(color.getRed()== 0xFF && color.getGreen()==0x80 && color.getBlue()==0x80){
                    value += 0.04;
                }
            }
        }
        return value;
    }

    private void feedNetwork(){
        int i=0;
        double samples[] = sampleImage();
        for( INeuron n : inputNeurons){
            ((InputNeuron) n).setValue(samples[i++]);
        }
    }

    private double calculateBWValue(Color color){
        return 0.33d*color.getRed()+ 0.33d*color.getGreen() + 0.34d*color.getBlue();
    }
}
