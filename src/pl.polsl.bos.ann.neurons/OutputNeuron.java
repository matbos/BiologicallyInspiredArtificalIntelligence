package pl.polsl.bos.ann.neurons;

import pl.polsl.bos.ann.Slot;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz Boś
 * Date: 14.05.13
 * Time: 22:09
 */
public class OutputNeuron extends Neuron {

    public OutputNeuron() {
        super();
    }

    public OutputNeuron(int inputs) {
        super(inputs+1);
    }

    public OutputNeuron(int inputs, int outputs) {
        super(inputs+1,outputs);
    }

    public OutputNeuron(ArrayList<Slot> inputs, ArrayList<INeuron> outputs) {
        super(inputs, outputs);
    }

    public OutputNeuron(ArrayList<INeuron> inputs) {
        super(inputs);
    }


    @Override
    public double recalculateError(double correctValue){
        error = activationFunctionDerivative(value) *(value - correctValue);
        // Update input weights
        for(Slot n : inputs){
                n.weight += -learningFactor * error * n.target.getValue();
        }
        upToDate = Boolean.TRUE;
        return error;
    }
}
