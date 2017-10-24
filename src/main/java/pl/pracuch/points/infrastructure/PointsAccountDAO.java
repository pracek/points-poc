package pl.pracuch.points.infrastructure;

import pl.pracuch.points.domain.PointsAccountId;
import pl.pracuch.points.web.PointsAccountViewModel;
import reactor.core.publisher.Mono;

public interface PointsAccountDAO {
    Mono<PointsAccountViewModel> byPointsAccountId(PointsAccountId id);
}
