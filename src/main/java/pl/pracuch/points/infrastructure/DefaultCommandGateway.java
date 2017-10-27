package pl.pracuch.points.infrastructure;

import nats.client.Nats;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DefaultCommandGateway implements CommandGateway {

    private final CommandHandlerProxy commandHandlerProxy;
    private Nats natsClient;

    public DefaultCommandGateway(CommandHandlerProxy commandHandlerProxy, Nats natsClient) {
        this.commandHandlerProxy = commandHandlerProxy;
        this.natsClient = natsClient;
    }

    @Override
    public Mono<Void> dispatch(Command command) {
        publishCommand(command);
        return commandHandlerProxy.handle(command);
    }

    private void publishCommand(Command command) {
        natsClient.publish("commands", command.toString());
    }
}
