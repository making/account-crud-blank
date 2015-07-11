package am.ik.archetype.domain.service.authentication;


import am.ik.archetype.domain.model.AuditLog;
import am.ik.archetype.domain.service.auditlog.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class AuthenticationFailureEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    @Autowired
    AuditLogService auditLogService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        Authentication authentication = event.getAuthentication();
        AuditLog auditLog = AuditLog.builder()
                .action("login")
                .domain("authenticate")
                .actor("----")
                .auditTime(Timestamp.valueOf(LocalDateTime.now()))
                .result(AuditLog.Result.FAILURE)
                .build();
        auditLogService.register(auditLog);
    }
}
