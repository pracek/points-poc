package pl.pracuch.points.infrastructure;

import reactor.core.publisher.Flux;

public interface CommandGateway {

    <R> Flux<R> dispatch(Command command);

}