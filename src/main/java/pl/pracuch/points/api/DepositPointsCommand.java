package pl.pracuch.points.api;


import lombok.Value;
import pl.pracuch.points.domain.PointsAccountId;

@Value(staticConstructor = "of")
public class DepositPointsCommand {
    private PointsAccountId pointsAccountId;
    private Integer amount;
}
