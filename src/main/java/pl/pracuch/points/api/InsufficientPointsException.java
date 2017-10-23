package pl.pracuch.points.api;

import pl.pracuch.points.domain.PointsAccountId;

public class InsufficientPointsException extends RuntimeException {
    private static final String TEMPLATE = "PointsAccount with id %s has insufficient balance for the operation";

    public InsufficientPointsException(PointsAccountId id) {
        super(String.format(TEMPLATE, id.toString()));
    }
}
