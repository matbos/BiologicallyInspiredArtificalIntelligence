package pl.polsl.bos.ann.neurons;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz Boś
 * Date: 12.05.13
 * Time: 22:05
 */

import pl.polsl.bos.ann.Slot;
import pl.polsl.bos.ann.exceptions.ErrorValueNotCalculatedException;
import pl.polsl.bos.ann.exceptions.NoConnectionBetweenThoseNeuronsException;

import java.util.ArrayList;

/**
 * Simple class representing neuron
 */
public abstract class Neuron implements INeuron {

    /**
     * Value of the neuron after calculation
     */
    protected double value;
    /**
     * Keeps list of all neurons in preceding layer which input is relevant to this neuron with their weights
     */
    protected ArrayList<Slot> inputs;
    /**
     * Keeps list of all neurons that recieve signal from this one.
     */
    protected ArrayList<INeuron> outputs;
    /**
     * This variable guards validity of neurons error.
     * True when value is valid
     * False when not
     */
    protected Boolean upToDate;
    /**
     * Calculated value of Neuron
     */
    public Double error;
    /**
     * ID of this Neuron used for serialization
     */
    protected Integer id;

    protected double learningFactor = 0.57814713;

    protected double betaValue = 1d;

    public Neuron() {
        inputs = new ArrayList<Slot>(10);
        outputs = new ArrayList<INeuron>(10);
    }

    public Neuron(int inputs) {
        this.inputs = new ArrayList<Slot>((inputs > 0) ? inputs : 10);
        outputs = new ArrayList<INeuron>(10);
    }

    public Neuron(int inputs, int outputs) {
        this.inputs = new ArrayList<Slot>((inputs > 0) ? inputs : 10);
        this.outputs = new ArrayList<INeuron>((outputs > 0) ? outputs : 10);
    }

    public Neuron(ArrayList<Slot> inputs, ArrayList<INeuron> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public Neuron(ArrayList<INeuron> inputs) {
        this(inputs.size());

        for (INeuron n : inputs) {
            this.inputs.add(new Slot(n, Math.random()));
        }
    }

    /**
     * @param inputs
     * @param outputs
     * @param fromSlots its value is ignored, can be null only used to distinguish between two constructors
     */
    public Neuron(ArrayList<INeuron> inputs, ArrayList<INeuron> outputs, Boolean fromSlots) {
        this(inputs.size(), outputs.size());

        for (INeuron n : inputs) {
            this.inputs.add(new Slot(n, Math.random()));
        }

        for (INeuron n : outputs) {
            this.outputs.add(n);
        }
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public double getError() throws ErrorValueNotCalculatedException {
        if (upToDate)
            return error;
        throw new ErrorValueNotCalculatedException();
    }
    @Override
    public void setNewIteration() {
        upToDate = false;
    }

    @Override
    public double recalculateValue(){
        value = activationFunction(getInducedLocalFieldOfEnergy());
        return value;
    }

    @Override
    public double getLearningFactor(){
        return learningFactor;
    }

    @Override
    public void setLearningFactor(double newFactor){
        learningFactor = newFactor;
    }
    @Override
    public double getWeightToMe(INeuron neuron){
        for( Slot slot : inputs){
            if (slot.target == neuron)
                return slot.weight;
        }
        throw new NoConnectionBetweenThoseNeuronsException("No connection between neurons with id "+getID() +" and "+ neuron.getID());
    }

    public void setOutputs(ArrayList<INeuron> outputs){
        this.outputs = outputs;
    }

    /**
     * Returns sum of all inputs with regard to their weight
     * @return sum of input*weight
     */
    protected double getInducedLocalFieldOfEnergy(){
        double sum = 0;
        for(Slot s : inputs){
            sum += s.weight * s.target.getValue();
        }
        return sum;
    }

    protected double activationFunction(double argument) {
        //return   1/(1 - Math.exp(-argument));
        return   1/(1+Math.exp(-betaValue*argument));
    }
    protected double activationFunctionDerivative(double argument) {
        //return   (1/(Math.cosh(value)*Math.cosh(value)));
        return betaValue*activationFunction(argument)*(1-activationFunction(argument));
    }

    protected void setValue(double newValue){
        value = newValue;
    }
}
