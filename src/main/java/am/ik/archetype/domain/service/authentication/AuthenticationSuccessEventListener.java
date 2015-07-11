package am.ik.archetype.domain.service.authentication;

import am.ik.archetype.domain.model.Account;
import am.ik.archetype.domain.model.AuditLog;
import am.ik.archetype.domain.service.auditlog.AuditLogService;
import am.ik.archetype.domain.service.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
    @Autowired
    AuditLogService auditLogService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        Account account = UserDetails.class.cast(authentication.getPrincipal()).getAccount();
        AuditLog auditLog = AuditLog.builder()
                .action("login")
                .domain("authenticate")
                .actor(account.getAccountId().toString())
                .auditTime(Timestamp.valueOf(LocalDateTime.now()))
                .result(AuditLog.Result.SUCCESS)
                .build();
        auditLogService.register(auditLog);
    }
}
