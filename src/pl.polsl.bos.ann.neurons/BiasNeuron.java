package pl.polsl.bos.ann.neurons;

import pl.polsl.bos.ann.Slot;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz Boś
 * Date: 22.05.13
 * Time: 21:14
 */
public class BiasNeuron extends Neuron {

    public BiasNeuron() {
        super();
    }

    public BiasNeuron(int inputs) {
        // This neuron has no inputs
        super();
    }

    public BiasNeuron(int inputs, int outputs) {
        // This neuron has no inputs
        super();
        this.outputs = new ArrayList<INeuron>(outputs);
    }

    public BiasNeuron(ArrayList<Slot> inputs, ArrayList<INeuron> outputs) {
        super();
        setOutputs(outputs);
    }

    public BiasNeuron(ArrayList<INeuron> inputs) {
        // This neuron has NO inputs
        super();
    }

    @Override
    public double recalculateError(double correctValue){
        // Its error Value is always 0
        error = 0d;
        return 0;
    }

    @Override
    public double recalculateValue(){
        // Its value is always 1
        value = 1d;
        return 1;
    }

}
