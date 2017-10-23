package pl.pracuch.points.web;

import io.vavr.control.Try;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.pracuch.points.api.DepositPointsCommand;
import pl.pracuch.points.domain.PointsAccount;
import pl.pracuch.points.domain.PointsAccountId;
import pl.pracuch.points.api.BurnPointsCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class PointsHandler {

    private static final PointsAccountId POINTS_ACCOUNT_ID = PointsAccountId.of("1beb4ecc-746d-44cd-9b83-0f0a68ef8eea");

    private Map<PointsAccountId, PointsAccount> points = new HashMap<>();

    public PointsHandler() {
        points.put(POINTS_ACCOUNT_ID, new PointsAccount(POINTS_ACCOUNT_ID));
    }

    public Mono<ServerResponse> deposit(ServerRequest serverRequest) {
        PointsAccountId pointsAccountId = PointsAccountId.of(serverRequest.pathVariable("accountId"));
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return getPointsAccountById(pointsAccountId)
                .flatMap(pointsAccount -> toDepositPointsCommand(serverRequest, pointsAccount.id()))
                // TODO: fire the command
                .flatMap(msg -> ServerResponse.ok().contentType(APPLICATION_JSON).build())
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> burnPoints(ServerRequest serverRequest) {
        PointsAccountId pointsAccountId = PointsAccountId.of(serverRequest.pathVariable("accountId"));
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return getPointsAccountById(pointsAccountId)
                .flatMap(pointsAccount -> toBurnPointsCommand(serverRequest, pointsAccount.id()))
                // TODO: fire the command
                .flatMap(msg -> ServerResponse.ok().contentType(APPLICATION_JSON).build())
                .switchIfEmpty(notFound);
    }

    private Mono<BurnPointsCommand> toBurnPointsCommand(ServerRequest request, PointsAccountId pointsAccountId) {
        return request.bodyToMono(AmountDTO.class).map(amountDto -> BurnPointsCommand.of(pointsAccountId, amountDto.getAmount()));
    }

    private Mono<DepositPointsCommand> toDepositPointsCommand(ServerRequest request, PointsAccountId pointsAccountId) {
        return request.bodyToMono(DepositDTO.class).map(depositDTO -> DepositPointsCommand.of(pointsAccountId, depositDTO.getAmount()));
    }

    private Mono<PointsAccount> getPointsAccountById(PointsAccountId pointsAccountId) {
        return Flux.fromIterable(points.entrySet())
                .filter(account -> pointsAccountId.equals(account.getValue().id()))
                .flatMap(entry -> Mono.justOrEmpty(entry.getValue()))
                .singleOrEmpty();
    }

}
