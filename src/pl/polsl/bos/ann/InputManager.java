package pl.polsl.bos.ann;

import pl.polsl.bos.ann.exceptions.NoConnectionBetweenThoseNeuronsException;
import pl.polsl.bos.ann.neurons.INeuron;
import pl.polsl.bos.ann.neurons.InputNeuron;

import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

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
        if (inputNeurons == null)
            throw new NoConnectionBetweenThoseNeuronsException();

        BufferedImage img = null;
        try{
            img = ImageIO.read(new File(pathToImage));
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
        if(img.getWidth() != 200 || img.getWidth() != img.getHeight() )
        {
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
        // jeśli w obrazie wystąpi pixel o kolorze F3F3F3
        // to jego wartość to suma pixeli
        double values[] = new double[1600];
        for(int counter = 0,  i=0; i<200; i+=5){
            for(int j = 0; j<200; j+=5){
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
                if(color.getRed() < 0xF3 && color.getGreen() < 0xF3 && color.getBlue() < 0xF3 ){
                    value += 0.16;
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
}
