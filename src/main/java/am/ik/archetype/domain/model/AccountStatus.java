package am.ik.archetype.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum AccountStatus {
    INIT("INT"), DISABLED("DSB"), ENABLED("ENB"), WILL_BE_DELETE("WBD"), UNKNOWN("UKW");
    private final String code;

    public static AccountStatus fromCode(String code) {
        return Stream.of(values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElse(UNKNOWN);
    }
}
