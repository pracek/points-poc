package pl.pracuch.points.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import pl.pracuch.points.api.InsufficientPointsException;
import pl.pracuch.points.api.PointsAccountNotFoundException;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.google.common.base.Predicates.instanceOf;
import static io.vavr.API.*;
import static io.vavr.control.Try.run;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Default exception handler for routes
 */
@Component
public class GlobalExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        Match(ex).of(
                Case($(instanceOf(PointsAccountNotFoundException.class)), o -> run(() -> setHttpStatus(exchange, NOT_FOUND, Optional.empty()))),
                Case($(instanceOf(InsufficientPointsException.class)), o -> run(() -> setHttpStatus(exchange, BAD_REQUEST, Optional.of(o.getMessage()))))
        );

        return Mono.empty();
    }

    private void setHttpStatus(ServerWebExchange exchange, HttpStatus status, Optional<String> reason) {
        exchange.getResponse().setStatusCode(status);
    }
}
