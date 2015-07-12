package am.ik.archetype.api.auditlog;

import am.ik.archetype.domain.model.AuditLog;
import am.ik.archetype.domain.service.auditlog.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auditlogs")
public class AuditLogRestController {
    @Autowired
    AuditLogService auditLogService;

    @RequestMapping
    Page<AuditLog> getAuditLogs(@PageableDefault Pageable pageable) {
        return auditLogService.findAll(pageable);
    }
}
