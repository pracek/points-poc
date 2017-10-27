package pl.pracuch.points;

import nats.client.Nats;
import nats.client.spring.EnableNatsAnnotations;
import nats.client.spring.NatsBuilder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@EnableAutoConfiguration
@EnableNatsAnnotations
@Configuration
public class ApplicationConfiguration {

    /**
     * Connect to nats.io server/cluster and expose client
     * @param applicationEventPublisher
     * @param environment
     * @return
     */
    @Bean
    public Nats nats(ApplicationEventPublisher applicationEventPublisher, Environment environment) {
        final Nats nats = new NatsBuilder(applicationEventPublisher)
                .addHost(environment.getProperty("nats-server", "nats://0.0.0.0:4222"))
                .connect();
        return nats;
    }
}
