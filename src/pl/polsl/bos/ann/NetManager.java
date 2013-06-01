package pl.polsl.bos.ann;

import com.tomgibara.CannyEdgeDetector;
import pl.polsl.bos.ann.exceptions.ErrorValueNotCalculatedException;
import pl.polsl.bos.ann.neurons.*;
import pl.polsl.bos.ui.Controller;

import javax.imageio.ImageIO;
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

    public NetManager() {
        this.layers = new ArrayList<ArrayList<INeuron>>(3);
        inputManager = new InputManager();
    }

    public NetManager(int inputCount, int hiddenCount, int outputCount) {
        this();
        initialize2LayerNeuralNet(inputCount, hiddenCount, outputCount);
        outputMappings = new String[outputCount];
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

    public void performTeaching(float lowTh,float highTh, File directory, double maxError, int iterations,Controller controller) {
        int numberOfOutputs, i=0;
        DirectoryList[] structure;

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
                    CannyEdgeDetector edgeDetector = new CannyEdgeDetector();
                    edgeDetector.setLowThreshold(lowTh);
                    edgeDetector.setHighThreshold(highTh);
                    edgeDetector.setSourceImage(image);
                    edgeDetector.process();
                    BufferedImage edgedImage = edgeDetector.getEdgesImage();
                    thisDirectory.addImage(edgedImage);
                    ImageIO.write(edgedImage,"jpg",edgedImageFile);
                } catch (IOException e) {
                    System.err.print(imageFile.getAbsolutePath()+ " |||| " + e.getMessage());
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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

/*        BufferedImage images[] = new BufferedImage[16];
        double[][] outcomes = new double[16][];
        int i = 0;
        images[i] = helperLoadImage("C:\\test\\0000.bmp");
        outcomes[i++] = convertDecimalTo1ofSizeArray(0, 16);
        images[i] = helperLoadImage("C:\\test\\0001.bmp");
        outcomes[i++] = convertDecimalTo1ofSizeArray(1, 16);
        images[i] = helperLoadImage("C:\\test\\0010.bmp");
        outcomes[i++] = convertDecimalTo1ofSizeArray(2, 16);
        images[i] = helperLoadImage("C:\\test\\0011.bmp");
        outcomes[i++] = convertDecimalTo1ofSizeArray(3, 16);
        images[i] = helperLoadImage("C:\\test\\0100.bmp");
        outcomes[i++] = convertDecimalTo1ofSizeArray(4, 16);
        images[i] = helperLoadImage("C:\\test\\0101.bmp");
        outcomes[i++] = convertDecimalTo1ofSizeArray(5, 16);
        images[i] = helperLoadImage("C:\\test\\0110.bmp");
        outcomes[i++] = convertDecimalTo1ofSizeArray(6, 16);
        images[i] = helperLoadImage("C:\\test\\0111.bmp");
        outcomes[i++] = convertDecimalTo1ofSizeArray(7, 16);
        images[i] = helperLoadImage("C:\\test\\1000.bmp");
        outcomes[i++] = convertDecimalTo1ofSizeArray(8, 16);
        images[i] = helperLoadImage("C:\\test\\1001.bmp");
        outcomes[i++] = convertDecimalTo1ofSizeArray(9, 16);
        images[i] = helperLoadImage("C:\\test\\1010.bmp");
        outcomes[i++] = convertDecimalTo1ofSizeArray(10, 16);
        images[i] = helperLoadImage("C:\\test\\1011.bmp");
        outcomes[i++] = convertDecimalTo1ofSizeArray(11, 16);
        images[i] = helperLoadImage("C:\\test\\1100.bmp");
        outcomes[i++] = convertDecimalTo1ofSizeArray(12, 16);
        images[i] = helperLoadImage("C:\\test\\1101.bmp");
        outcomes[i++] = convertDecimalTo1ofSizeArray(13, 16);
        images[i] = helperLoadImage("C:\\test\\1110.bmp");
        outcomes[i++] = convertDecimalTo1ofSizeArray(14, 16);
        images[i] = helperLoadImage("C:\\test\\1111.bmp");
        outcomes[i++] = convertDecimalTo1ofSizeArray(15, 16);
        */
        //wlasciwe uczenie sieci
        for (i = 0; i < iterations; ++i) {
            inputManager.feedImage(structure[i % structure.length].getNextImage());
            propagateSignalToOutput();
            introduceCorrections(structure[i%structure.length].getProperOutcome());
            //if (allNeuronsHaveErrorLessThan(maxError))
            //    break;
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
//        for (int j = 0; j <= 15; ++j) {
//            inputManager.feedImage(images[j]);
//            propagateSignalToOutput();
//            printResult(j);
//        }
    }

    public String recognizeImage(BufferedImage image){
        inputManager.feedImage(image);
        propagateSignalToOutput();
        return outputMappings[determineBestMatch()];
    }
    private void printResult(int correct) {
        int i;
        double outcome[] = gatherOutputSignals();
        i = 0;
        int max = 0;
        for (double d : outcome) {
            if (d > outcome[max])
                max = i;
            i++;
        }
        System.out.println("########################################");
        System.out.println(String.format("# %3s # %2d ## %2d # %.12f ######", (correct == max) ? "OK" : "NOT", correct, max, outcome[max]));
        System.out.println("########################################");
        if (DETAILS) {
            i = 0;
            ArrayList<INeuron> outputLayer = outputLayer();
            for (double d : outcome) {
                try {
                    System.out.println(String.format("%2d | %.12f | %.12f", i, d, outputLayer.get(i++).getError()));
                } catch (ErrorValueNotCalculatedException e) {

                }
            }
            System.out.println("=-==================================================================");
        }
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

    private BufferedImage helperLoadImage(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            // TODO: error loading image, some communicate ?!
            System.out.println(e.getStackTrace());
        }
        if (img.getWidth() != 200 || img.getWidth() != img.getHeight()) {
            // TODO: niepoprawny plik został załadowany, poprawny rozmiar to 200x200 px, obsłużyć to
            throw new RuntimeException("Niepoprawna wielkość obrazka! " + path);
        }
        return img;
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

    private double[] getError() {
        int i = outputLayer().size();
        double error[] = new double[i];
        i = 0;
        for (INeuron n : outputLayer()) {
            try {
                error[i++] = n.getError();
            } catch (ErrorValueNotCalculatedException e) {
                throw new RuntimeException(e);
            }
        }
        return error;
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
        double array[] = new double[size];
        for(int j  =0; j < size; ++j)
            array[j] = 0f;
        array[i] = 1f;
        return array;
    }

    private double[] convertDecimalTo1ofSizeArray(int number, int size) {
        double array[] = new double[size];
        for (int i = 0; i < size; ++i)
            array[i] = 0d;
        array[number] = 1d;
        return array;
    }

    static public int countDirectories(File[] files){
        int count =0;
        for(File f : files)
            if(f.isDirectory())
                count++;
        return  count;
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
