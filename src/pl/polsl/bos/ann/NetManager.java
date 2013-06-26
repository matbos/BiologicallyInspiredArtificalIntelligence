package pl.polsl.bos.ann;

import com.tomgibara.CannyEdgeDetector;
import pl.polsl.bos.ann.exceptions.ErrorValueNotCalculatedException;
import pl.polsl.bos.ann.neurons.*;
import pl.polsl.bos.ui.Controller;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz Boś
 * Date: 12.05.13
 * Time: 22:43
 */
public class NetManager {
    // ArrayList containing all layers within the net
    private ArrayList<ArrayList<INeuron>> layers;
    private InputManager inputManager;
    private final boolean DETAILS = false;
    private double errorSQ = 0.0D;
    private String[] outputMappings;
    private ArrayList<Mapping> outputMapping;
    public NetManager() {
        this.layers = new ArrayList<ArrayList<INeuron>>(3);
        inputManager = new InputManager();
    }

    public NetManager(int inputCount, int hiddenCount, int outputCount) {
        this();
        initialize2LayerNeuralNet(inputCount, hiddenCount, outputCount);
        outputMappings = new String[outputCount];
    }


    public ArrayList<Mapping> getMakes() {
        for(Mapping m : outputMapping){
            try{
                m.setError(outputLayer().get(m.getNumber()).getError());
            } catch(ErrorValueNotCalculatedException e) {

            }
        }
       return outputMapping;
    }
    /**
     * Method returning list iterator for input layer
     *
     * @return List operator of input Neuron objects
     */
    public ArrayList<INeuron> inputLayer() {
        return layers.get(0);
    }

    /**
     * Method returning hidden layer list
     *
     * @return List operator of hidden Neuron objects
     */
    public ArrayList<INeuron> hiddenLayer() {
        if (layers.size() == 3)
            return layers.get(1);
        else
            throw new UnsupportedOperationException("There is too many(>3) or not enough(<3) layers in the network, rendering this method moot");
    }

    /**
     * Method returning output layer list
     *
     * @return List operator of output Neuron objects
     */
    public ArrayList<INeuron> outputLayer() {
        return layers.get(layers.size() - 1);
    }

    /**
     * Creates neutral network with two layers and proper amount of neurons on each,
     * connected all-to-all between layers
     *
     * @param inputCount  number of input nodes
     * @param hiddenCount number of hidden nodes
     * @param outputCount number of output nodes
     */
    public void initialize2LayerNeuralNet(int inputCount, int hiddenCount, int outputCount) {
        // Initialize layers to be ready for proper amounts of neurons
        layers.add(new ArrayList<INeuron>(inputCount));
        layers.add(new ArrayList<INeuron>(hiddenCount));
        layers.add(new ArrayList<INeuron>(outputCount));

        ArrayList<INeuron> output = layers.get(2);
        ArrayList<INeuron> hidden = layers.get(1);
        ArrayList<INeuron> input = layers.get(0);

        for (int i = 0; i < inputCount; ++i) {
            input.add(new InputNeuron());
        }

        for (int i = 0; i < hiddenCount; ++i) {
            hidden.add(new HiddenNeuron(input));
        }

        for (int i = 0; i < outputCount; ++i) {
            output.add(new OutputNeuron(hidden));
        }
        //
        for (INeuron n : hidden) {
            n.setOutputs(output);
        }
        inputManager.setInputNeurons(inputLayer());
    }

    public void performTeaching(File directory, double maxError, int iterations,Controller controller) {
        int numberOfOutputs, i=0;
        DirectoryList[] structure;
        outputMapping = new ArrayList<Mapping>(5);
        File directories[] = directory.listFiles();
        numberOfOutputs = countDirectories(directories);
        directories =  clearNonDirectories(directories, numberOfOutputs);
        structure = new DirectoryList[numberOfOutputs];

        i=0;
        int j = 0;

        for(File dir : directories){

            File edgedDir = new File(dir.getAbsolutePath().concat("/Edged"));

            if(!edgedDir.mkdir())
            {
                // OBSŁUGA BRAKU MOŻLIWOŚCI STWORZENIA FOLDERU
            }


            File[] images = dir.listFiles();
            structure[i] = new DirectoryList(dir.getName(),getArrayWithOne(i,structure.length),images);
            outputMapping.add(new Mapping(dir.getName(),i));
            outputMappings[j++] = dir.getName();
            DirectoryList thisDirectory = structure[i++];

            for  (File imageFile : images){
                if (!imageFile.isFile())
                    continue;
                try {
                    File edgedImageFile = new File(edgedDir.getAbsolutePath().concat("/"+imageFile.getName()));
                    BufferedImage image = ImageIO.read(imageFile);
                    if (image == null ){
                        System.out.println(" NULL IMAGE with : "+ imageFile.getAbsolutePath());
                        continue;
                    }
                    if(image.getHeight() != 200 || image.getWidth() != 200){
                        System.out.println(" Image with inappropriate size! : "+ imageFile.getAbsolutePath());
                        continue;
                    }
                        for(int x=0; x<image.getHeight(); ++x){
                            for(int y = 0; y < image.getWidth();++y){
                                Color color = new Color(image.getRGB(x,y));
                                if(color.getRed()!= 0xFF || color.getGreen()!=0xFF || color.getBlue()!=0xFF){
                                    image.setRGB(x,y,0);
                                }
                            }
                        }
                    thisDirectory.addImage(image);
                    ImageIO.write(image,"jpg",edgedImageFile);
                } catch (IOException e) {
                    System.err.print(imageFile.getAbsolutePath()+ " |||| " + e.getMessage());
                    e.printStackTrace();
                } catch (Exception e ) {
                    System.err.println("-----------------");
                    System.err.println(e.getMessage());
                    System.err.println("-----------------");
                    e.printStackTrace();
                    System.err.println("-----------------");
                    throw new RuntimeException(imageFile.getAbsolutePath(),e);
                }

            }
            System.out.println(dir.getAbsolutePath()+" skończona!");
        }
        System.out.println("DONE!");


        //wlasciwe uczenie sieci
        for (i = 0; i < iterations; ++i) {
            inputManager.feedImage(structure[i % structure.length].getNextImage());
            propagateSignalToOutput();
            introduceCorrections(structure[i%structure.length].getProperOutcome());
            if((errorSQ - maxError) <= 0.0)
                break;
            if(i%100 == 0){
                controller.giveFeedback(i,errorSQ);
                System.err.print(".");
            }
        }
        System.out.println("Iteracji: "+i);
        compareError(maxError);
        controller.giveFeedback(i,errorSQ);
    }

    public String recognizeImage(BufferedImage image){
        inputManager.feedImage(image);
        propagateSignalToOutput();
        return outputMappings[determineBestMatch()];
    }

    public double[] getHiddenErrors(){
        double[] errors = new double[hiddenLayer().size()];
        int i =0;
        try {
            for (INeuron n : hiddenLayer())
                errors[i++] = n.getError();
        } catch (ErrorValueNotCalculatedException e) {
            System.out.println("Podczas zczytywania błędów ukrytych");
            System.out.println(e.getMessage());
        }
        return errors;
    }

    public double[] getOutputErrors(){
        double[] errors = new double[outputLayer().size()];
        int i =0;
        try {
            for (INeuron n : outputLayer())
                errors[i++] = n.getError();
        } catch (ErrorValueNotCalculatedException e) {
            System.out.println("Podczas zczytywania błędów wyjścia");
            System.out.println(e.getMessage());
        }
        return errors;
    }

    static public int countDirectories(File[] files){
        int count =0;
        for(File f : files)
            if(f.isDirectory())
                count++;
        return  count;
    }

    private void propagateSignalToOutput() {
        for (INeuron n : hiddenLayer())
            n.recalculateValue();
        for (INeuron n : outputLayer())
            n.recalculateValue();
        double[] out = gatherOutputSignals();
        out[1] = out[1] * 1;
    }

    private void introduceCorrections(double correctVector[]) {
        int j = 0;
        errorSQ = 0.0D;
        for (INeuron n : outputLayer()) {
            double error = n.recalculateError(correctVector[j]);
            errorSQ += Math.pow((correctVector[j++]-error),2.0D);
        }
        errorSQ *= 0.5D;
        for (INeuron n : hiddenLayer())
            n.recalculateError(0); // passed value is not important since, in this layer correct value is calucated.
    }

    private double[] gatherOutputSignals() {
        double output[] = new double[outputLayer().size()];
        int i = 0;
        for (INeuron n : outputLayer())
            output[i++] = n.getValue();
        return output;
    }

    private boolean allNeuronsHaveErrorLessThan(double value) {
        for (INeuron n : outputLayer()) {
            Double error = Double.valueOf(((Neuron) n).error);
            if (error < Double.valueOf(value))
                return false;
        }
        return true;
    }

    private void compareError(double val) {
        double error[] = gatherOutputSignals();
        System.out.println("########################################");
        for (double d : error) {
            System.out.println(String.format("## %3s ## %.15f ############", (d > val) ? "NOT" : "OK", d));
        }
        System.out.println("########################################");
    }

    private double[] getArrayWithOne(int i, int size){
        double array[] = new double[size+1];
        for(int j  =0; j < size; ++j)
            array[j] = 0f;
        array[size]= 0f;
        array[i] = 1f;
        return array;
    }

    private File[] clearNonDirectories(File[] list, int numberOfDirectories){
        File[] directories = new File[numberOfDirectories];
        int i=0;
        for(File f : list){
            if(f.isDirectory())
                directories[i++] = f;
        }
        return directories;
    }

    private int determineBestMatch()     {
        int output = 0, i=0;
        double value = -1;
        for(INeuron n : outputLayer()){
            if(n.getValue() > value)
            {
                value = n.getValue();
                output = i;
            }
            i++;
        }
        return output;
    }

}
