package am.ik.archetype.domain.service.login;

import am.ik.archetype.domain.model.FailedLoginAttempt;
import am.ik.archetype.domain.repository.login.FailedLoginAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FailedLoginAttemptServiceImpl implements FailedLoginAttemptService {
    @Autowired
    FailedLoginAttemptRepository failedLoginAttemptRepository;

    @Override
    public FailedLoginAttempt save(FailedLoginAttempt failedLoginAttempt) {
        return failedLoginAttemptRepository.save(failedLoginAttempt);
    }
}
