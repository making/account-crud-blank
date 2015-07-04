package am.ik.archetype.domain.service.account;

import am.ik.archetype.domain.model.Account;
import am.ik.archetype.domain.model.Email;
import am.ik.archetype.domain.model.Password;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {
    boolean isUnusedEmail(Email email);

    boolean isUnusedEmailOtherThanMe(Email email, Long accountId);

    Account findOne(Long accountId);

    Page<Account> findAll(Pageable pageable);

    Account create(Account account, Password rawPassword);

    Account updateWithoutPassword(Account account, boolean unlock);

    Account updateWithNewPassword(Account account, Password rawPassword, boolean unlock);

    Account rehashIfNeeded(Account account, Password rawPassword);

    void delete(Long accountId);

    boolean isLocked(Account account);
}
