package mariuszs.model;

import clojure.lang.Ref;

public class SafeBalance implements Balance {

    private Ref balance;

    public SafeBalance(long initialBalance) {
        this.balance = new Ref(initialBalance);
    }

    @Override
    public long getValue() {
        return (Long) balance.deref();
    }

    @Override
    public void subtract(long amount) {
        balance.set(getValue() - amount);
    }

    @Override
    public void add(long amount) {
        balance.set(getValue() + amount);
    }
}
