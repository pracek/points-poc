package pl.pracuch.points.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.pracuch.points.api.BurnPointsCommand;
import pl.pracuch.points.domain.PointsAccountRepository;
import pl.pracuch.points.infrastructure.CommandHandler;
import reactor.core.publisher.Mono;

@Component
public class BurnPointsCommandHandler extends CommandHandler<BurnPointsCommand> {

    private PointsAccountRepository pointsAccountRepository;

    @Autowired
    public BurnPointsCommandHandler(PointsAccountRepository pointsAccountRepository) {
        this.pointsAccountRepository = pointsAccountRepository;
    }

    @Override
    public Mono<Void> handle(BurnPointsCommand command) {
        return pointsAccountRepository
                .get(command.getPointsAccountId())
                .flatMap(pointsAccount -> pointsAccount.burnPoints(command.getAmount()))
                .flatMap(pointsAccountRepository::save);
    }
}
