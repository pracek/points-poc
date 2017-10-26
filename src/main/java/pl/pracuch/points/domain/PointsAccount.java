package pl.pracuch.points.domain;

import pl.pracuch.points.api.InsufficientPointsException;
import reactor.core.publisher.Mono;

public class PointsAccount {
    private final PointsAccountId id;
    private final Integer balance;

    public PointsAccount(final PointsAccountId id, final Integer initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public PointsAccount(final PointsAccountId id) {
        this(id, 0);
    }

    public Mono<PointsAccount> burnPoints(final Integer spent) {
        return canBurnPoints(spent) ? Mono.just(new PointsAccount(id, balance - spent)) : Mono.error(new InsufficientPointsException(id));
    }

    public PointsAccount depositPoints(final Integer deposit) {
        return new PointsAccount(this.id, this.balance + deposit);
    }

    public boolean canBurnPoints(final Integer amount) {
        return balance - amount >= 0;
    }

    public PointsAccountId id() {
        return id;
    }

    public Integer balance() {return balance; }
}
