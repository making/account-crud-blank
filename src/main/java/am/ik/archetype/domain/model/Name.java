package am.ik.archetype.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Name implements Serializable {
    @NotNull
    @Size(min = 1, max = 128)
    private String value;

    @Override
    public String toString() {
        return value;
    }
}
