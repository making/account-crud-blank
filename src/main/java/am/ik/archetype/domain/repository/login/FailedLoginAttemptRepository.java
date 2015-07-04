package am.ik.archetype.domain.repository.login;

import am.ik.archetype.domain.model.FailedLoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FailedLoginAttemptRepository extends JpaRepository<FailedLoginAttempt, FailedLoginAttempt.Id> {
    @Modifying
    @Query(value = "DELETE FROM FailedLoginAttempt x WHERE x.attemptId.account.accountId = :accountId")
    int deleteByAccountId(@Param("accountId") Long accountId);

    @Query(value = "SELECT COUNT(x) FROM FailedLoginAttempt x WHERE x.attemptId.account.accountId = :accountId")
    long countByAccountId(@Param("accountId") Long accountId);
}
