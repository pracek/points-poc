package pl.pracuch.points;

import io.vavr.control.Try;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.*;
import pl.pracuch.points.web.PointsHandler;
import pl.pracuch.points.web.StatusHandler;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @Bean
    public RouterFunction<ServerResponse> _status(StatusHandler statusHandler, PointsHandler pointsHandler) {
        return route(GET("/_status"), statusHandler::_status)
                .andRoute(GET("/points/{accountId}").and(accept(APPLICATION_JSON)), pointsHandler::getPointsAccount)
                .andRoute(POST("/points/{accountId}/deposits").and(accept(APPLICATION_JSON)), pointsHandler::deposit)
                .filter(operationIdHeaderRequiredFilter())
                .andRoute(POST("/points/{accountId}/spendings").and(accept(APPLICATION_JSON)), pointsHandler::burnPoints)
                .filter(operationIdHeaderRequiredFilter());
    }

    private HandlerFilterFunction<ServerResponse, ServerResponse> operationIdHeaderRequiredFilter() {
        return (request, next) -> {
            if (request.headers().header("Operation-Id").size() == 0) {
                return ServerResponse.status(BAD_REQUEST).body(fromObject("Required \"Operation-Id\" header is missing"));
            }

            Try<UUID> candidate = Try.of(() -> UUID.fromString(request.headers().header("Operation-Id").get(0)));
            if (candidate.isFailure()) {
                return ServerResponse.status(BAD_REQUEST).body(fromObject("\"Operation-Id\" header is not a valid UUID"));
            }
            else {
                return next.handle(request);
            }
        };
    }
}
