package pl.pracuch.points.domain;

import io.vavr.control.Try;
import pl.pracuch.points.api.InsufficientPointsException;

public class PointsAccount {
    private PointsAccountId id;
    private Integer balance;

    public PointsAccount(final PointsAccountId id, final Integer initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public PointsAccount(final PointsAccountId id) {
        this(id, 0);
    }

    public Try<PointsAccount> burnPoints(final Integer spent) {
        return canBurnPoints(spent) ? Try.success(new PointsAccount(id, balance - spent)) : Try.failure(new InsufficientPointsException(id));
    }

    public boolean canBurnPoints(final Integer amount) {
        return balance - amount >= 0;
    }

    public PointsAccountId id() {
        return id;
    }

    public Integer balance() {return balance; }
}
