package am.ik.archetype.domain.aspect.auditlog;

import am.ik.archetype.domain.model.AuditLog;
import am.ik.archetype.domain.repository.auditlog.AuditLogRepository;
import am.ik.archetype.domain.service.userdetails.UserDetails;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Parameter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Aspect
@Component
public class AuditLogAspect {
    @Autowired
    AuditLogRepository auditLogRepository;

    @Around("execution(* * (..)) && @annotation(audit)")
    public Object audit(ProceedingJoinPoint point, Audit audit) throws Throwable {
        Object result = null;
        String action = StringUtils.isEmpty(audit.action()) ? point.getSignature().getName() :
                audit.action();
        String domain = StringUtils.isEmpty(audit.domain()) ? point.getTarget().getClass().getSimpleName() :
                audit.domain();
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setDomain(domain);
        auditLog.setAuditTime(Timestamp.valueOf(LocalDateTime.now()));

        try {
            result = point.proceed();
            auditLog.setResult(AuditLog.Result.SUCCESS);
        } catch (Exception e) {
            auditLog.setResult(AuditLog.Result.FAILURE);
            throw e;
        } finally {
            MethodSignature signature = MethodSignature.class.cast(point.getSignature());
            Optional<Object> opt = getTargetFromArgs(signature, point.getArgs());
            if (opt.isPresent()) {
                auditLog.setTarget(opt.get().toString());
            } else {
                getTargetFromResult(signature, result).ifPresent(x -> {
                    auditLog.setTarget(x.toString());
                });
            }

            String actor = getUsername();
            if (actor != null) {
                auditLog.setActor(actor);
            } else {
                auditLog.setActor("system");
            }
            auditLogRepository.save(auditLog);
        }
        return result;
    }

    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return UserDetails.class.cast(principal).getAccount().getAccountId().toString();
            }
            return principal.toString();
        }
        return null;
    }

    Optional<Object> getTargetFromArgs(MethodSignature signature, Object[] args) {
        Parameter[] parameters = signature.getMethod().getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            AuditTarget auditTarget = parameter.getDeclaredAnnotation(AuditTarget.class);
            if (auditTarget != null) {
                return getTarget(args[i], auditTarget);
            }
        }
        return Optional.empty();
    }

    Optional<Object> getTargetFromResult(MethodSignature signature, Object result) {
        AuditTarget auditTarget = signature.getMethod().getDeclaredAnnotation(AuditTarget.class);
        if (auditTarget != null) {
            return getTarget(result, auditTarget);
        }
        return Optional.empty();
    }

    Optional<Object> getTarget(Object object, AuditTarget auditTarget) {
        if (object == null) {
            return Optional.empty();
        }
        if (StringUtils.isEmpty(auditTarget.value())) {
            return Optional.of(object);
        }
        BeanWrapper beanWrapper = new BeanWrapperImpl(object);
        return Optional.of(beanWrapper.getPropertyValue(auditTarget.value()));
    }

}
