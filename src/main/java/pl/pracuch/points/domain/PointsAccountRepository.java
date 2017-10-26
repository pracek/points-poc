package pl.pracuch.points.domain;

import reactor.core.publisher.Mono;

public interface PointsAccountRepository {
    Mono<Void> save(PointsAccount pointsAccount);
    Mono<PointsAccount> get(PointsAccountId pointsAccountId);
}
