package pl.pracuch.points.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@Component
public class DefaultCommandHandlerProxy implements CommandHandlerProxy {

    private List<CommandHandler> commandHandlers;

    @Autowired
    public DefaultCommandHandlerProxy(List<CommandHandler> commandHandlers) {
        this.commandHandlers = commandHandlers;
    }

    @Override
    public Mono<Void> handle(Command command) {
        for (CommandHandler ch : commandHandlers) {
            if (command.getClass().equals(getCommandHandlerType(ch))) {
                return ch.handle(command);
            }
        }
        return Mono.error(new IllegalArgumentException("No handler found for " + command.getClass()));
    }

    public static Class<?> getCommandHandlerType(CommandHandler instance) {
        return (Class<?>) ((ParameterizedType) instance.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}