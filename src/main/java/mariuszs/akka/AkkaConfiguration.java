package mariuszs.akka;

import akka.actor.ActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static mariuszs.akka.SpringExtension.SpringExtProvider;

@Configuration
public class AkkaConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("ReActBoot");
        SpringExtProvider.get(system).initialize(applicationContext);
        return system;
    }
}
