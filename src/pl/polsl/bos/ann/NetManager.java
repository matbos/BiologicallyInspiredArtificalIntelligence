package pl.polsl.bos.ann;

import pl.polsl.bos.ann.exceptions.ErrorValueNotCalculatedException;
import pl.polsl.bos.ann.neurons.*;

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

    public NetManager() {
        this.layers = new ArrayList<ArrayList<INeuron>>(3);
        inputManager = new InputManager();
    }

    public NetManager(int inputCount, int hiddenCount, int outputCount) {
        this();
        initialize2LayerNeuralNet(inputCount, hiddenCount, outputCount);
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
        /*TODO: Dodaj tutaj propagację następnych elementów w sieci zaczynając od warstwy wyjściowej trzeba dodać odpowiednie metody w neuronach aby powiadamiały  swoim istnieniu wszystkich poprzedników.
                Przemyśl to!
        */
    }

    public void teach(BufferedImage image, double desiredOutput[]) {
        inputManager.feedImage(image);

    }

    public void performTeaching() {
        BufferedImage images[] = new BufferedImage[16];
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
        //wlasciwe uczenie sieci
        for (i = 0; i < 5000000; ++i) {
            inputManager.feedImage(images[i % 16]);
            propagateSignalToOutput();
            introduceCorrections(outcomes[i % 16]);
            if (allNeuronsHaveErrorLessThan(0.0009))
                break;
        }
        System.out.println("Iteracji: "+i);
        compareError(0.0009);
        for (int j = 0; j <= 15; ++j) {
            inputManager.feedImage(images[j]);
            propagateSignalToOutput();
            printResult(j);
        }
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
        for (INeuron n : outputLayer()) {
            n.recalculateError(correctVector[j++]);

        }
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

    private double[] convertDecimalTo1ofSizeArray(int number, int size) {
        double array[] = new double[size];
        for (int i = 0; i < size; ++i)
            array[i] = 0d;
        array[number] = 1d;
        return array;
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
}
