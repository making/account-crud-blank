package am.ik.archetype.domain.service.authentication;


import am.ik.archetype.domain.model.Account;
import am.ik.archetype.domain.model.AuditLog;
import am.ik.archetype.domain.service.auditlog.AuditLogService;
import am.ik.archetype.domain.service.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class AuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {
    @Autowired
    AuditLogService auditLogService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
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

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
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
