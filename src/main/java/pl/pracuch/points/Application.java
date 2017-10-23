package pl.pracuch.points;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.pracuch.points.web.PointsHandler;
import pl.pracuch.points.web.StatusHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
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
                .andRoute(POST("/points/{accountId}/deposits").and(accept(APPLICATION_JSON)), pointsHandler::deposit)
                .andRoute(POST("/points/{accountId}/spendings").and(accept(APPLICATION_JSON)), pointsHandler::burnPoints);
    }

}
