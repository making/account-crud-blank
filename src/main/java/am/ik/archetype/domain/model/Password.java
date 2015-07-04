package am.ik.archetype.domain.model;

import am.ik.archetype.domain.validation.groups.CrudMode;
import am.ik.archetype.infra.password.HashAlgorithmAttributeConverter;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.stream.Stream;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Password implements Serializable {
    @NotNull(groups = CrudMode.Create.class)
    @Size(min = 6, max = 128, groups = CrudMode.Create.class)
    private String value;

    @NotNull
    @Convert(converter = HashAlgorithmAttributeConverter.class)
    private HashAlgorithm algorithm = HashAlgorithm.NO;

    public Password encode(PasswordEncoder passwordEncoder, HashAlgorithm algorithm) {
        if (this.algorithm == algorithm) {
            return this;
        }
        return new Password(passwordEncoder.encode(value), algorithm);
    }

    public static Password raw(String rawPassword) {
        return new Password(rawPassword, HashAlgorithm.NO);
    }

    public static Password encoded(String password, HashAlgorithm algorithm) {
        return new Password(password, algorithm);
    }

    @AllArgsConstructor
    @Getter
    public enum HashAlgorithm {
        NO("HS00"), BCRYPT("HS01"), SHA256("HS02"), UNKOWN("---");
        private final String code;

        public static HashAlgorithm fromCode(String code) {
            return Stream.of(values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElse(UNKOWN);
        }
    }
}
