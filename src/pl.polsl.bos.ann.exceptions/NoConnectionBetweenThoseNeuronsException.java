package pl.polsl.bos.ann.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz Boś
 * Date: 20.05.13
 * Time: 22:23
 */
public class NoConnectionBetweenThoseNeuronsException extends RuntimeException{
    public NoConnectionBetweenThoseNeuronsException(){
        super();
    }

    public NoConnectionBetweenThoseNeuronsException(String message){
        super(message);
    }
}
