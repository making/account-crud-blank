package am.ik.archetype.domain.service.login;


import am.ik.archetype.domain.model.FailedLoginAttempt;

public interface FailedLoginAttemptService {
    FailedLoginAttempt save(FailedLoginAttempt failedLoginAttempt);
}
