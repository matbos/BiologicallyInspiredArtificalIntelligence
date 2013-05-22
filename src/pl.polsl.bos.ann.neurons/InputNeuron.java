package pl.polsl.bos.ann.neurons;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz Bos;
 * Date: 20.05.13
 * Time: 21:54
 */
public class InputNeuron extends Neuron {
    @Override
    public double recalculateError(double correctValue){
        throw new UnsupportedOperationException("Input neurons cannot have their error recalculated!!!");
    }

    public void setValue(double d){
        value = d;
    }
}
