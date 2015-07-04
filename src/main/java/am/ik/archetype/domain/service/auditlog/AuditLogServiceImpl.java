package am.ik.archetype.domain.service.auditlog;

import am.ik.archetype.domain.model.AuditLog;
import am.ik.archetype.domain.repository.auditlog.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AuditLogServiceImpl implements AuditLogService {
    @Autowired
    AuditLogRepository auditLogRepository;

    @Override
    public Page<AuditLog> findAll(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }


    @Scheduled(initialDelay = 600_000, fixedRate = 600_000)
    void report() {
        auditLogRepository.findAll().forEach(
                System.out::println
        );
    }
}
