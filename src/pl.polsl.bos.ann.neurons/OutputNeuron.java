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
     //   this.inputs.add(new Slot(new BiasNeuron(),Math.random()));
    }

    public OutputNeuron(int inputs) {
        super(inputs+1);
    //    this.inputs.add(new Slot(new BiasNeuron(),Math.random()));
    }

    public OutputNeuron(int inputs, int outputs) {
        super(inputs+1,outputs);
    //    this.inputs.add(new Slot(new BiasNeuron(),Math.random()));
    }

    public OutputNeuron(ArrayList<Slot> inputs, ArrayList<INeuron> outputs) {
        super(inputs, outputs);
     //   this.inputs.add(new Slot(new BiasNeuron(),Math.random()));
    }

    public OutputNeuron(ArrayList<INeuron> inputs) {
        super(inputs);
     //   this.inputs.add(new Slot(new BiasNeuron(),Math.random()));
    }


    @Override
    public double recalculateError(double correctValue){
        //error = value * (1 - value) *(value - correctValue);
        error = activationFunctionDerivative(value) *(value - correctValue);
        // Update input weights
        for(Slot n : inputs){
                n.weight += -learningFactor * error * n.target.getValue();
                double superValue = n.weight * 1;
        }

        upToDate = Boolean.TRUE;
        return error;
    }
}
