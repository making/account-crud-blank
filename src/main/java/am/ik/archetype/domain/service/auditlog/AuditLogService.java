package am.ik.archetype.domain.service.auditlog;

import am.ik.archetype.domain.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {
    Page<AuditLog> findAll(Pageable pageable);
}
