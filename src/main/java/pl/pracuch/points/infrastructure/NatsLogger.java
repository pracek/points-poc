package pl.pracuch.points.infrastructure;

import nats.client.Message;
import nats.client.Nats;
import nats.client.spring.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NatsLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(NatsLogger.class);
    private Nats nats;

    @Autowired
    NatsLogger(Nats nats) {
        this.nats = nats;
    }

    @Subscribe("*")
    public void onMessage(Message message) {
        LOGGER.info("Received: {}", message);
    }


}
