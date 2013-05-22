package pl.polsl.bos.ann.neurons;

import pl.polsl.bos.ann.Slot;
import pl.polsl.bos.ann.exceptions.ErrorValueNotCalculatedException;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz Boś
 * Date: 15.05.13
 * Time: 14:16
 */
public class HiddenNeuron extends Neuron {

    public HiddenNeuron() {
        super();
    }

    public HiddenNeuron(int inputs) {
        super(inputs);
    }

    public HiddenNeuron(int inputs, int outputs) {
        super(inputs,outputs);
    }

    public HiddenNeuron(ArrayList<Slot> inputs, ArrayList<INeuron> outputs) {
        super(inputs, outputs);
    }

    public HiddenNeuron(ArrayList<INeuron> inputs) {
        super(inputs);
    }

    @Override
    public double recalculateError(double correctValue){
        double sumOfNextLayerError = 0;
        for(INeuron n : outputs){
            try {
                double weightToMe = n.getWeightToMe((INeuron)this);
                sumOfNextLayerError += n.getError() * weightToMe;
            } catch (ErrorValueNotCalculatedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        //error = value * (1 - value) * sumOfNextLayerError;
        error = activationFunctionDerivative(value) * sumOfNextLayerError;
        // Update input weights
        for(Slot n : inputs){
            n.weight += -learningFactor * error * n.target.getValue();
            double vval = n.weight * 1;
        }

        upToDate = Boolean.TRUE;
        return error;
    }
}
