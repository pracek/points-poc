package pl.pracuch.points.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.pracuch.points.api.BurnPointsCommand;
import pl.pracuch.points.api.DepositPointsCommand;
import pl.pracuch.points.api.PointsAccountNotFoundException;
import pl.pracuch.points.domain.PointsAccountId;
import pl.pracuch.points.domain.PointsAccountRepository;
import pl.pracuch.points.infrastructure.CommandGateway;
import pl.pracuch.points.infrastructure.PointsAccountDAO;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class PointsHandler {

    private PointsAccountRepository pointsAccountRepository;
    private PointsAccountDAO pointsAccountDAO;
    private CommandGateway commandGateway;

    @Autowired
    public PointsHandler(PointsAccountRepository pointsAccountRepository, PointsAccountDAO pointsAccountDAO, CommandGateway commandGateway) {
        this.pointsAccountRepository = pointsAccountRepository;
        this.pointsAccountDAO = pointsAccountDAO;
        this.commandGateway = commandGateway;
    }

    public Mono<ServerResponse> getPointsAccount(ServerRequest serverRequest) {
        PointsAccountId pointsAccountId = PointsAccountId.of(serverRequest.pathVariable("accountId"));
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<PointsAccountViewModel> pointsAccountMono = pointsAccountDAO.byPointsAccountId(pointsAccountId);

        return pointsAccountMono
                .flatMap(pointsAccount -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(pointsAccount)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deposit(ServerRequest serverRequest) {
        PointsAccountId pointsAccountId = PointsAccountId.of(serverRequest.pathVariable("accountId"));
        Mono<ServerResponse> commandDispatchResult = pointsAccountRepository
                .get(pointsAccountId)
                .switchIfEmpty(Mono.error(new PointsAccountNotFoundException()))
                .flatMap(pointsAccount -> toDepositPointsCommand(serverRequest, pointsAccount.id(), operationId(serverRequest)))
                .flatMap(commandGateway::dispatch)
                .flatMap(emptyJustToChangeType -> ServerResponse.unprocessableEntity().build());
        return Mono
                .first(commandDispatchResult, ServerResponse.ok().contentType(APPLICATION_JSON).build());
    }

    public Mono<ServerResponse> burnPoints(ServerRequest serverRequest) {
        PointsAccountId pointsAccountId = PointsAccountId.of(serverRequest.pathVariable("accountId"));
        Mono<ServerResponse> commandDispatchResult = pointsAccountRepository
                .get(pointsAccountId)
                .switchIfEmpty(Mono.error(new PointsAccountNotFoundException()))
                .flatMap(pointsAccount -> toBurnPointsCommand(serverRequest, pointsAccount.id(), operationId(serverRequest)))
                .flatMap(commandGateway::dispatch)
                .flatMap(emptyJustToChangeType -> ServerResponse.unprocessableEntity().build());
        return Mono
                .first(commandDispatchResult, ServerResponse.ok().contentType(APPLICATION_JSON).build());
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
