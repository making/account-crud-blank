package am.ik.archetype.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditLogId;
    @NotNull
    private String domain;
    @NotNull
    private String actor;
    private String target;
    @NotNull
    private String action;
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Result result;
    @NotNull
    private Timestamp auditTime;

    public enum Result {
        FAILURE, SUCCESS
    }
}
