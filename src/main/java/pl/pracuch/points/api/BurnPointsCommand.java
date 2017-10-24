package pl.pracuch.points.api;

import lombok.Value;
import pl.pracuch.points.domain.PointsAccountId;
import pl.pracuch.points.infrastructure.Command;

import java.util.UUID;

@Value(staticConstructor = "of")
public class BurnPointsCommand implements Command {
    private UUID commandId;
    private PointsAccountId pointsAccountId;
    private Integer amount;
}
