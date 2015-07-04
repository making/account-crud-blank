package am.ik.archetype.domain.repository.auditlog;


import am.ik.archetype.domain.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
