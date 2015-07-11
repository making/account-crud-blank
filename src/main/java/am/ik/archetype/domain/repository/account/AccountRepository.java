package am.ik.archetype.domain.repository.account;

import am.ik.archetype.domain.model.Account;
import am.ik.archetype.domain.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    long countByEmail_value(String email);

    Optional<Account> findByEmail_value(String email);

    List<Account> findByAccountStatus(AccountStatus accountStatus);
}
