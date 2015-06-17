package am.ik.archetype.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BirthDate implements Serializable {
    @NotNull
    @Past
    @Temporal(TemporalType.DATE)
    private Date value;

    public BirthDate(LocalDate birthDate) {
        this.setValue(birthDate);
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public void setValue(LocalDate value) {
        if (value != null) {
            this.value = java.sql.Date.valueOf(value);
        }
    }

    public LocalDate getValue() {
        if (this.value == null) {
            return null;
        }
        if (this.value instanceof java.sql.Date) {
            return java.sql.Date.class.cast(this.value).toLocalDate();
        } else {
            return this.value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }
}