package mariuszs;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.RoundRobinPool;
import mariuszs.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static mariuszs.akka.SpringExtension.SpringExtProvider;


@Component
public class Bank  {

    private static final Logger log = LoggerFactory.getLogger(Bank.class);

    @Autowired
    ActorSystem system;

    @Autowired
    AccountService accountService;

    public void run(String... args) throws Exception {

//        System.out.println(">>> " + );

        long old = sum();

        // use the Spring Extension to create props for a named actor bean
        ActorRef account = system.actorOf(
                SpringExtProvider.get(system).props("AccountActor")
                        .withRouter(new RoundRobinPool(1000)), "account");

        // tell it to count three times

        Random random = new Random(4);

        for (int i = 0; i < 10_000_000; i++) {

            account.tell(new AccountActor.Transfer(random.nextInt(5),
                    random.nextInt(99),
                    random.nextInt(99)), null);
        }

        try {
            system.awaitTermination(Duration.create(10, TimeUnit.SECONDS));
        } catch (Exception e) {

        }

        System.out.println("Was: " + old + " and is now: " + sum());
        System.out.println("Min " + accountService.balances().values().stream()
                .mapToLong(Account::getBalance).min());
        System.out.println("Max " + accountService.balances().values().stream()
                .mapToLong(Account::getBalance).max());
    }

    private long sum() {
        return accountService.balances().values().stream().mapToLong(Account::getBalance).sum();
    }
}
