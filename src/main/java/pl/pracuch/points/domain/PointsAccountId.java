package pl.pracuch.points.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.UUID;

@Value(staticConstructor = "of")
@Accessors(fluent = true)
public class PointsAccountId {
    @JsonValue
    private final String id;

    public static PointsAccountId randomId() {
        return PointsAccountId.of(UUID.randomUUID().toString());
    }
}
