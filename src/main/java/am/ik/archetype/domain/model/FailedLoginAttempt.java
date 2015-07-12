package am.ik.archetype.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(indexes = {
        @Index(columnList = "account_id"),
        @Index(columnList = "attemptTime")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailedLoginAttempt implements Serializable {
    @EmbeddedId
    private Id attemptId;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Id implements Serializable {
        @NotNull
        @ManyToOne
        @JoinColumn(name = "account_id")
        private Account account;

        @NotNull
        private Timestamp attemptTime;
    }
}
