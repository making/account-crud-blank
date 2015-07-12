package am.ik.archetype.domain.service.authentication;


import am.ik.archetype.domain.model.AuditLog;
import am.ik.archetype.domain.model.FailedLoginAttempt;
import am.ik.archetype.domain.repository.account.AccountRepository;
import am.ik.archetype.domain.service.auditlog.AuditLogService;
import am.ik.archetype.domain.service.login.FailedLoginAttemptService;
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
    @Autowired
    FailedLoginAttemptService failedLoginAttemptService;
    @Autowired
    AccountRepository accountRepository;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        Authentication authentication = event.getAuthentication();
        String email = (String) authentication.getPrincipal();
        String actor = accountRepository.findByEmail_value(email)
                .map(account -> {
                    FailedLoginAttempt attempt = new FailedLoginAttempt(
                            new FailedLoginAttempt.Id(account, Timestamp.valueOf(LocalDateTime.now())));
                    failedLoginAttemptService.save(attempt);
                    return account.getAccountId().toString();
                })
                .orElse("----");

        AuditLog auditLog = AuditLog.builder()
                .action("login")
                .domain("authenticate")
                .actor(actor)
                .auditTime(Timestamp.valueOf(LocalDateTime.now()))
                .result(AuditLog.Result.FAILURE)
                .build();
        auditLogService.register(auditLog);
    }
}
