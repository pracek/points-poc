package pl.pracuch.points.infrastructure;

import org.springframework.stereotype.Component;
import pl.pracuch.points.domain.PointsAccount;
import pl.pracuch.points.domain.PointsAccountId;
import pl.pracuch.points.domain.PointsAccountRepository;
import pl.pracuch.points.wev.PointsAccountViewModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryPointsAccountRepositoryAndDAO implements PointsAccountRepository, pl.pracuch.points.infrastructure.PointsAccountDAO {

    public static final PointsAccountId INITIAL_POINTS_ACCOUNT_ID = PointsAccountId.of("1beb4ecc-746d-44cd-9b83-0f0a68ef8eea");

    private Map<PointsAccountId, PointsAccount> points = new HashMap<>();

    public InMemoryPointsAccountRepositoryAndDAO() {
        points.put(INITIAL_POINTS_ACCOUNT_ID, new PointsAccount(INITIAL_POINTS_ACCOUNT_ID));
    }


    @Override
    public void save(PointsAccount pointsAccount) {
        points.put(pointsAccount.id(), pointsAccount);
    }

    @Override
    public Mono<PointsAccount> get(PointsAccountId pointsAccountId) {
        return Flux.fromIterable(points.entrySet())
                .filter(account -> pointsAccountId.equals(account.getValue().id()))
                .flatMap(entry -> Mono.justOrEmpty(entry.getValue()))
                .singleOrEmpty();
    }

    @Override
    public Mono<PointsAccountViewModel> byPointsAccountId(PointsAccountId pointsAccountId) {
        return Flux.fromIterable(points.entrySet())
                .filter(account -> pointsAccountId.equals(account.getValue().id()))
                .flatMap(entry -> Mono.justOrEmpty(entry.getValue()))
                .map(this::toViewModel).
                singleOrEmpty();
    }

    private PointsAccountViewModel toViewModel(PointsAccount pointsAccount) {
        return new PointsAccountViewModel(pointsAccount.id(), pointsAccount.balance());
    }
}
