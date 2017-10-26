package pl.pracuch.points.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.pracuch.points.api.DepositPointsCommand;
import pl.pracuch.points.domain.PointsAccountRepository;
import pl.pracuch.points.infrastructure.CommandHandler;
import reactor.core.publisher.Mono;

@Component
public class DepositPointsCommandHandler extends CommandHandler<DepositPointsCommand> {

    private PointsAccountRepository pointsAccountRepository;

    @Autowired
    public DepositPointsCommandHandler(PointsAccountRepository pointsAccountRepository) {
        this.pointsAccountRepository = pointsAccountRepository;
    }

    @Override
    public Mono<Void> handle(DepositPointsCommand command) {
        return pointsAccountRepository
                .get(command.getPointsAccountId())
                .map(pointsAccount -> pointsAccount.depositPoints(command.getAmount()))
                .flatMap(pointsAccountRepository::save);
    }
}
