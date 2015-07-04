package am.ik.archetype.domain.service.account;

import am.ik.archetype.domain.aspect.auditlog.Audit;
import am.ik.archetype.domain.aspect.auditlog.AuditTarget;
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

    @AuditTarget("accountId")
    Account create(Account account, Password rawPassword);

    Account updateWithoutPassword(@AuditTarget("accountId") Account account, boolean unlock);

    Account updateWithNewPassword(@AuditTarget("accountId") Account account, Password rawPassword, boolean unlock);

    Account rehashIfNeeded(@AuditTarget("accountId") Account account, Password rawPassword);

    void delete(@AuditTarget Long accountId);

    boolean isLocked(Account account);
}
