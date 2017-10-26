package pl.pracuch.points.infrastructure;

import reactor.core.publisher.Mono;

public abstract class CommandHandler<C extends Command> {
    public abstract Mono<Void> handle(C command);
}
