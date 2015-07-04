package am.ik.archetype.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum AccountState {
    INIT("INT"), DISABLED("DSB"), ENABLED("ENB"), WILL_BE_DELETE("WBD"), UNKNOWN("UKW");
    private final String code;

    public static AccountState fromCode(String code) {
        return Stream.of(values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElse(UNKNOWN);
    }
}
