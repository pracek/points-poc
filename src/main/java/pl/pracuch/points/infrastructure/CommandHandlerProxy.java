package pl.pracuch.points.infrastructure;

import reactor.core.publisher.Mono;

public interface CommandHandlerProxy {
    Mono<Void> handle(Command command);
}
