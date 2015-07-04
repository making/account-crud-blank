package am.ik.archetype.domain.service.account;

import am.ik.archetype.domain.aspect.auditlog.Audit;
import am.ik.archetype.domain.model.Account;
import am.ik.archetype.domain.model.Credential;
import am.ik.archetype.domain.model.Email;
import am.ik.archetype.domain.model.Password;
import am.ik.archetype.domain.repository.account.AccountRepository;
import am.ik.archetype.domain.repository.login.FailedLoginAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    FailedLoginAttemptRepository failedLoginAttemptRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    Password.HashAlgorithm systemHashAlgorithm;

    @Value("${failedloginattempt.threshold:5}")
    int failedLoginAttemptThreshold;

    @Transactional(readOnly = true)
    @Override
    public boolean isUnusedEmail(Email email) {
        return accountRepository.countByEmail_value(email.getValue()) == 0;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isUnusedEmailOtherThanMe(Email email, Long accountId) {
        Account account = accountRepository.findOne(accountId);
        return account.getEmail().equals(email) || isUnusedEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public Account findOne(Long accountId) {
        return accountRepository.findOne(accountId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Account> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Audit(domain = "account")
    @Override
    public Account create(Account account, Password rawPassword) {
        Credential credential = new Credential();
        credential.setPassword(rawPassword.encode(passwordEncoder, systemHashAlgorithm));
        account.setCredential(credential);
        return accountRepository.save(account);
    }

    @Audit(domain = "account")
    @Override
    public Account updateWithoutPassword(Account account, boolean unlock) {
        Assert.notNull(account.getCredential(), "Password must not be null.");
        Account updated = accountRepository.save(account);
        if (unlock) {
            failedLoginAttemptRepository.deleteByAccountId(account.getAccountId());
        }
        return updated;
    }

    @Audit(domain = "account")
    @Override
    public Account updateWithNewPassword(Account account, Password rawPassword, boolean unlock) {
        account.getCredential().setPassword(rawPassword.encode(passwordEncoder, systemHashAlgorithm));
        Account updated = accountRepository.save(account);
        if (unlock) {
            failedLoginAttemptRepository.deleteByAccountId(account.getAccountId());
        }
        return updated;
    }

    @Audit(domain = "account")
    @Override
    public Account rehashIfNeeded(Account account, Password rawPassword) {
        if (systemHashAlgorithm == account.getCredential().getPassword().getAlgorithm()) {
            return account;
        }
        account.getCredential().setPassword(rawPassword.encode(passwordEncoder, systemHashAlgorithm));
        return accountRepository.save(account);
    }

    @Audit(domain = "account")
    @Override
    public void delete(Long accountId) {
        accountRepository.delete(accountId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isLocked(Account account) {
        return failedLoginAttemptRepository.countByAccountId(account.getAccountId()) >= failedLoginAttemptThreshold;
    }
}
