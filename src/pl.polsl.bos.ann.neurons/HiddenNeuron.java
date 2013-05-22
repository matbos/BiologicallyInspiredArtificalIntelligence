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
       // inputs.add(new Slot(new BiasNeuron(),Math.random()));
    }

    public HiddenNeuron(int inputs) {
        super(inputs+1);
      //  this.inputs.add(new Slot(new BiasNeuron(),Math.random()));
    }

    public HiddenNeuron(int inputs, int outputs) {
        super(inputs+1,outputs);
      //  this.inputs.add(new Slot(new BiasNeuron(),Math.random()));
    }

    public HiddenNeuron(ArrayList<Slot> inputs, ArrayList<INeuron> outputs) {
        super(inputs, outputs);
      //  this.inputs.add(new Slot(new BiasNeuron(),Math.random()));
    }

    public HiddenNeuron(ArrayList<INeuron> inputs) {
        super(inputs);
      //  this.inputs.add(new Slot(new BiasNeuron(),Math.random()));
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
