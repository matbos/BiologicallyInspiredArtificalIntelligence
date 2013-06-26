package pl.polsl.bos.ui;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz Boś
 * Date: 22.06.13
 * Time: 16:57
 */
public class LearningSet {
    private Double hidden;
    private Double output;

    public LearningSet(double hidden, double output){
        this.hidden = hidden;
        this.output = output;
    }
    public LearningSet(double hidden){
        this.hidden = hidden;
    }
    public Double getHidden(){
        return hidden;
    }

    public Double getOutput() {
        return output;
    }

    public void setOutput(Double output) {
        this.output = output;
    }

    public void setHidden(Double hidden){
        this.hidden = hidden;
    }
}
