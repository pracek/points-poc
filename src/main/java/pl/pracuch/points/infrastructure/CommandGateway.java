package pl.pracuch.points.infrastructure;

import reactor.core.publisher.Mono;

public interface CommandGateway {

    <R> Mono<R> dispatch(Command command);

}