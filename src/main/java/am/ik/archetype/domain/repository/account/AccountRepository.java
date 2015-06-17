package am.ik.archetype.domain.repository.account;

import am.ik.archetype.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    long countByEmail_value(String email);
}
