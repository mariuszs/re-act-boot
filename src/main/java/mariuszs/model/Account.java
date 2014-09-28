package mariuszs.model;

public class Account {

    public static final boolean inTransaction = false;

    private final Integer id;

    private Balance balance;

    public Account(Integer id, Long initialBalance) {
        this.id = id;
        if (inTransaction)
            balance = new SafeBalance(initialBalance);
        else
            balance = new UnsafeBalance(initialBalance);
    }

    public void withdraw(final long amount) throws Exception {
        if (amount < 0) throw new RuntimeException("Invalid amount");
        if (amount > getBalance()) throw new RuntimeException("Insufficient fund");
        balance.subtract(amount);
    }

    public void withdrawOnlyValid(final long amount) throws Exception {
        if (amount < 0) throw new RuntimeException("Invalid amount");

    }

    public void deposit(final long amount) throws Exception {
        if (amount < 0) throw new RuntimeException("Invalid amount");

        balance.add(amount);

    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + getBalance() +
                '}';
    }

    public long getBalance() {
        return balance.getValue();
    }

}
