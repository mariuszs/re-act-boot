package mariuszs.model;

public class UnsafeBalance implements Balance {
    private long balance;

    public UnsafeBalance(long balance) {
        this.balance = balance;
    }

    @Override
    public long getValue() {
        return balance;
    }

    @Override
    public void subtract(long amount) {
        balance -= amount;
    }

    @Override
    public void add(long amount) {
        balance += amount;
    }
}
