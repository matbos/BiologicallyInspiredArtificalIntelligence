package pl.polsl.bos.ann.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz Boś
 * Date: 14.05.13
 * Time: 20:50
 */
public class ErrorValueNotCalculatedException extends Exception {

    public ErrorValueNotCalculatedException(){
        super();
    }

    public ErrorValueNotCalculatedException(String msg){
        super(msg);
    }

}
