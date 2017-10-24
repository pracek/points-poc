package pl.pracuch.points.wev;

import lombok.Data;
import pl.pracuch.points.domain.PointsAccountId;

@Data
public class PointsAccountViewModel {
    private final PointsAccountId pointsAccountId;

    private final Integer balance;
}
