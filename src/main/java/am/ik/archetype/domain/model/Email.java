package am.ik.archetype.domain.model;

import am.ik.archetype.domain.validation.UnusedEmail;
import am.ik.archetype.domain.validation.groups.CrudMode;
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
public class Email implements Serializable {
    @NotNull
    @Size(min = 1, max = 128)
    @org.hibernate.validator.constraints.Email
    @UnusedEmail(groups = CrudMode.Create.class)
    private String value;

    @Override
    public String toString() {
        return value;
    }
}
