package mariuszs;

import mariuszs.model.Account;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class Reporter {

    private static final Logger log = LoggerFactory.getLogger(Reporter.class);

    private final AccountService accountService;

    @Autowired
    public Reporter(AccountService accountService) {
        this.accountService = accountService;
    }

    @Scheduled(fixedRate = 5000)
    public void report() {
        final Collection<Account> accounts = accountService.balances().values();
        log.warn("Total balance : ${}\t Number of transactions: {} (invalid: {})\t\t min=${}.\t max=${},\t average=${}",
                accountService.balances().values().stream().mapToLong(Account::getBalance).sum(),
                StringUtils.leftPad(String.valueOf(accountService.getTransactionCount()), 5),
                StringUtils.leftPad(String.valueOf(accountService.getInvalidTransactionCount()), 5),
                accounts.stream().mapToLong(Account::getBalance).min().orElseGet(() -> -1),
                accounts.stream().mapToLong(Account::getBalance).max().orElseGet(() -> -1),
                accounts.stream().mapToLong(Account::getBalance).average().orElseGet(() -> -1)
        );
    }

    private long sum() {
        return accountService.balances().values().stream().mapToLong(Account::getBalance).sum();
    }
}


