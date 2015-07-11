package am.ik.archetype.domain.service.account;

import am.ik.archetype.domain.aspect.auditlog.Audit;
import am.ik.archetype.domain.model.Account;
import am.ik.archetype.domain.model.AccountStatus;
import am.ik.archetype.domain.repository.account.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AccountCleaner {
    @Autowired
    AccountRepository accountRepository;


    @Audit(domain = "account")
    @Scheduled(fixedDelay = 3600_000, initialDelay = 3600_000)
    @Transactional
    public void cleanup() {
        log.info("Delete obsolete accounts");
        List<Account> accounts = accountRepository.findByAccountStatus(AccountStatus.WILL_BE_DELETE);
        accountRepository.delete(accounts);
        log.info("Deleted");
    }
}
