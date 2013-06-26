package pl.polsl.bos.ui;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz Boś
 * Date: 31.05.13
 * Time: 18:37
 */
public interface Progressable {
    public void giveFeedback(int iteration, double error);

}
