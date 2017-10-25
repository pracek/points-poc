package pl.pracuch.points.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.pracuch.points.api.BurnPointsCommand;
import pl.pracuch.points.api.DepositPointsCommand;
import pl.pracuch.points.domain.PointsAccountId;
import pl.pracuch.points.domain.PointsAccountRepository;
import pl.pracuch.points.infrastructure.PointsAccountDAO;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static io.vavr.API.$;
import static io.vavr.API.Match;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class PointsHandler {

    private PointsAccountRepository pointsAccountRepository;
    private PointsAccountDAO pointsAccountDAO;

    @Autowired
    public PointsHandler(PointsAccountRepository pointsAccountRepository, PointsAccountDAO pointsAccountDAO) {
        this.pointsAccountRepository = pointsAccountRepository;
        this.pointsAccountDAO = pointsAccountDAO;
    }

    public Mono<ServerResponse> getPointsAccount(ServerRequest serverRequest) {
        PointsAccountId pointsAccountId = PointsAccountId.of(serverRequest.pathVariable("accountId"));
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<PointsAccountViewModel> pointsAccountMono = pointsAccountDAO.byPointsAccountId(pointsAccountId);

        return pointsAccountMono
//                .flatMap(pointsAccount -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject("{\"abc\": \"abc\"}")))
                .flatMap(pointsAccount -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(pointsAccount)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deposit(ServerRequest serverRequest) {
        PointsAccountId pointsAccountId = PointsAccountId.of(serverRequest.pathVariable("accountId"));
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return pointsAccountRepository
                .get(pointsAccountId)
                .flatMap(pointsAccount -> toDepositPointsCommand(serverRequest, pointsAccount.id(), operationId(serverRequest)))
                // TODO: fire the command
                .flatMap(msg -> ServerResponse.ok().contentType(APPLICATION_JSON).build())
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> burnPoints(ServerRequest serverRequest) {
        PointsAccountId pointsAccountId = PointsAccountId.of(serverRequest.pathVariable("accountId"));
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return pointsAccountRepository
                .get(pointsAccountId)
                .flatMap(pointsAccount -> toBurnPointsCommand(serverRequest, pointsAccount.id(), operationId(serverRequest)))
                // TODO: fire the command
                .flatMap(msg -> ServerResponse.ok().contentType(APPLICATION_JSON).build())
                .switchIfEmpty(notFound);
    }

    private Mono<BurnPointsCommand> toBurnPointsCommand(ServerRequest request, PointsAccountId pointsAccountId, UUID operationId) {
        return request
                .bodyToMono(AmountDTO.class)
                .map(amountDto -> BurnPointsCommand.of(operationId, pointsAccountId, amountDto.getAmount()));
    }

    private Mono<DepositPointsCommand> toDepositPointsCommand(ServerRequest request, PointsAccountId pointsAccountId, UUID operationId) {
        return request
                .bodyToMono(DepositDTO.class)
                .map(depositDTO -> DepositPointsCommand.of(operationId, pointsAccountId, depositDTO.getAmount()));
    }

    private UUID operationId(ServerRequest request) {
        return UUID.fromString(request.headers().header("Operation-Id").get(0));
    }
}
