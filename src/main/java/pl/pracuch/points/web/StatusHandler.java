package pl.pracuch.points.web;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class StatusHandler {
    public Mono<ServerResponse> _status(ServerRequest serverRequest) {
        return ServerResponse.ok().body(Mono.just("OK"), String.class);
    }
}
