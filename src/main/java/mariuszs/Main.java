package mariuszs;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import mariuszs.CountingActor.Count;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;
import static mariuszs.akka.SpringExtension.SpringExtProvider;

/**
 * A main class to start up the application.
 */
@Component
public class Main implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @Autowired
    ActorSystem system;

    @Override
    public void run(String... args) throws Exception {

        // use the Spring Extension to create props for a named actor bean
        ActorRef counter = system.actorOf(
                SpringExtProvider.get(system).props("CountingActor"), "counter");

        // tell it to count three times
        counter.tell(new Count(), null);
        counter.tell(new Count(), null);
        counter.tell(new Count(), null);

        // print the result
        FiniteDuration duration = FiniteDuration.create(10, TimeUnit.SECONDS);
        Future<Object> result = ask(counter, new CountingActor.Get(),
                Timeout.durationToTimeout(duration));
        try {
            log.info("Got back {}", Await.result(result, duration));
        } catch (Exception e) {
            log.error("Failed getting result!", e);
        } finally {
            system.shutdown();
            system.awaitTermination();
        }
    }
}
