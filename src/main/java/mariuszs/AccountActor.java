package mariuszs;

import akka.actor.UntypedActor;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import javax.inject.Named;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Named("AccountActor")
@Scope(SCOPE_PROTOTYPE)
public class AccountActor extends UntypedActor {

    final AccountService accountService;

    @Inject
    public AccountActor(@Named("AccountService") AccountService accountService) {
        this.accountService = accountService;
    }

    public static class Transfer {
        long amount;
        int from;
        int to;

        public Transfer(long amount, int from, int to) {
            this.amount = amount;
            this.from = from;
            this.to = to;
        }
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof Transfer) {
            Transfer transfer = (Transfer) msg;
            accountService.transfer(transfer);
        } else {
            unhandled(msg);
        }
    }
}
