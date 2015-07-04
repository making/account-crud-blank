package am.ik.archetype.domain.repository.account;

import am.ik.archetype.domain.model.Account;
import am.ik.archetype.domain.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    long countByEmail_value(String email);

    List<Account> findByAccountStatus(AccountStatus accountStatus);
}
