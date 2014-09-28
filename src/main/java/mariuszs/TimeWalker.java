package mariuszs;

import mariuszs.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class TimeWalker {

    private static final Logger log = LoggerFactory.getLogger(TimeWalker.class);

    private final AccountService accountService;
    final Random random = new Random();

    @Autowired
    public TimeWalker(AccountService accountService) {
        this.accountService = accountService;
    }

    @Scheduled(fixedRate = 50)
    public void report() {

        final int size = accountService.balances().size();

        for (int i = 0; i < random.nextInt(1000); i++) {
            final int amount = random.nextInt(10);
            final int from = random.nextInt(size);
            final int to = random.nextInt(size);
            transfer(amount, from, to);
        }
    }

    private void transfer(int amount, int from, int to) {
        try {
            accountService.transfer(new AccountActor.Transfer(amount,
                    from,
                    to));
        } catch (Exception e) {

        }
    }

    private long sum() {


        return accountService.balances().values().stream().mapToLong(Account::getBalance).sum();
    }
}


