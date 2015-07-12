package am.ik.archetype.domain.repository.login;

import am.ik.archetype.domain.model.FailedLoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedLoginAttemptRepository extends JpaRepository<FailedLoginAttempt, FailedLoginAttempt.Id> {

    int deleteByAttemptId_account_accountId(Long accountId);

    long countByAttemptId_account_accountId(Long accountId);
}
