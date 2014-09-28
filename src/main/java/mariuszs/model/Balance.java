package mariuszs.model;

public interface Balance {


    long getValue();

    void subtract(long amount);

    void add(long amount);
}
