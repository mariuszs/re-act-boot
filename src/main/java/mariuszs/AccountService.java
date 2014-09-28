package mariuszs;

import clojure.lang.LockingTransaction;
import mariuszs.model.Account;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Named("AccountService")
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    private Map<Integer, Account> accounts = new HashMap<>();

    private AtomicLong transactionCount = new AtomicLong(0);
    private AtomicLong invalidTransactionCount = new AtomicLong(0);

    @PostConstruct
    void initialize() {
        for (int i = 0; i < 100; i++) {
            accounts.put(i, new Account(i, 10L));
        }
    }

    @Async
    public void transferOnlyValid(AccountActor.Transfer transfer) throws Exception {

        try {
            LockingTransaction.runInTransaction(
                    () -> {
                        Account accountFrom = get(transfer.from);
                        accountFrom.withdrawOnlyValid(transfer.amount);
                        Account accountTo = get(transfer.to);
                        accountTo.deposit(transfer.amount);
                        TimeUnit.MILLISECONDS.sleep(RandomUtils.nextInt(1, 10));
                        return null;
                    });
            transactionCount.incrementAndGet();
        } catch (Exception e) {
            invalidTransactionCount.incrementAndGet();
        }
    }

    @Async
    public void transfer(AccountActor.Transfer transfer) throws Exception {
        try {
            Account accountFrom = get(transfer.from);
            Account accountTo = get(transfer.to);

            LockingTransaction.runInTransaction(
                    () -> {

                        accountFrom.withdraw(transfer.amount);

                        TimeUnit.MILLISECONDS.sleep(RandomUtils.nextInt(1, 10));

                        accountTo.deposit(transfer.amount);

                        return null;
                    });
            transactionCount.incrementAndGet();
        } catch (Exception e) {
            invalidTransactionCount.incrementAndGet();
        }
    }

    private Account get(int accountNumber) {
        return accounts.get(accountNumber);
    }

    public Map<Integer, Account> balances() {
        return Collections.unmodifiableMap(accounts);
    }

    public long getTransactionCount() {
        return transactionCount.get();
    }

    public long getInvalidTransactionCount() {
        return invalidTransactionCount.get();
    }

}
