package pl.pracuch.points.infrastructure;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DefaultCommandGateway implements CommandGateway {

    private final CommandHandlerProxy commandHandlerProxy;

    public DefaultCommandGateway(CommandHandlerProxy commandHandlerProxy) {
        this.commandHandlerProxy = commandHandlerProxy;
    }

    @Override
    public Mono<Void> dispatch(Command command) {
        return commandHandlerProxy.handle(command);
    }
}
