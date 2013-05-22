package pl.polsl.bos.ann.neurons;

import pl.polsl.bos.ann.exceptions.ErrorValueNotCalculatedException;
import java.util.ArrayList;
/**
 * Created with IntelliJ IDEA.
 * User: Mateusz Boś
 * Date: 14.05.13
 * Time: 19:51
 */
public interface INeuron {
    /**
     * Returns id of given neuron
     * @return
     */
    int getID();
    /**
     * Returns current value of neuron
     * @return current value of neuron
     */
    double getValue();
    /**
     * Returns error calculated earlier
     * @return
     * @throws pl.polsl.bos.ann.exceptions.ErrorValueNotCalculatedException when error value has not been recalculated for this iteration
     */
    double getError() throws ErrorValueNotCalculatedException;
    /**
     * Calculates error value
     * @param correctValue correct value of this neuron
     * @return error of this neuron
     */
    double recalculateError(double correctValue);
    /**
     * Method used to indicate that value of this neuron should be invalid from now on.
     */
    void setNewIteration();
    /**
     * Recalculates value of this neuron
     * @return
     */
    double recalculateValue();
    /**
     * Returns value of learning factor (eta)
     * @return value of learning factor
     */
    double getLearningFactor();
    /**
     * * Sets value of learning factor (eta)
     * @param newFactor factor to be set
     */
    void setLearningFactor(double newFactor);
    /**
     * Method returns weight of the connection between its object(the one it was called on)
     * and @neuron from previous layer. Effectively returns weight of input connection.
     * @param neuron
     * @return weight between this neuron and param neuron
     */
    public double getWeightToMe(INeuron neuron);
    /**
     *
     * @param output
     */
    public void setOutputs(ArrayList<INeuron> output);
}
