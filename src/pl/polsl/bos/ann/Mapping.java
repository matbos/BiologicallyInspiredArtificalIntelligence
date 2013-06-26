package pl.polsl.bos.ann;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz Boś
 * Date: 22.06.13
 * Time: 17:49
 */
public class Mapping {
    private int number;
    private String name;
    private Double error;

    public Mapping(String name, int number){
        this.name = name;
        this.number = number;
    }
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getError() {
        return error;
    }

    public void setError(Double error) {
        this.error = error;
    }
}
