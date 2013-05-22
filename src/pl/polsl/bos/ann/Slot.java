package pl.polsl.bos.ann;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz Boś
 * Date: 14.05.13
 * Time: 21:22
 */

import pl.polsl.bos.ann.neurons.INeuron;

/**
 * Class representing input with its weight
 */
public class Slot {
    public double weight;
    public INeuron target;

    public Slot(INeuron target, double weight) {
        this.weight = weight;
        this.target = target;
    }
}
