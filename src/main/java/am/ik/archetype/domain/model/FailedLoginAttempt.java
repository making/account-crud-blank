package am.ik.archetype.domain.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(indexes = {
        @Index(columnList = "account"),
        @Index(columnList = "attemptTime")
})
@Data
public class FailedLoginAttempt implements Serializable {
    @EmbeddedId
    private Id attemptId;

    @Embeddable
    @Data
    public static class Id implements Serializable {
        @NotNull
        @ManyToOne
        @JoinColumn(name = "account")
        private Account account;

        @NotNull
        private Timestamp attemptTime;
    }
}
