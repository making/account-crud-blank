package am.ik.archetype.domain.service.account;

import am.ik.archetype.domain.model.Account;
import am.ik.archetype.domain.model.Email;
import am.ik.archetype.domain.model.Password;
import am.ik.archetype.domain.repository.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    Password.HashAlgorithm systemHashAlgorithm;

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

    @Override
    public Account create(Account account, Password rawPassword) {
        account.setPassword(rawPassword.encode(passwordEncoder, systemHashAlgorithm));
        return accountRepository.save(account);
    }

    @Override
    public Account update(Account account, Password rawPassword) {
        account.setPassword(rawPassword.encode(passwordEncoder, systemHashAlgorithm));
        return accountRepository.save(account);
    }

    @Override
    public Account rehashIfNeeded(Account account, Password rawPassword) {
        if (systemHashAlgorithm == account.getPassword().getAlgorithm()) {
            return account;
        }
        account.setPassword(rawPassword.encode(passwordEncoder, systemHashAlgorithm));
        return accountRepository.save(account);
    }

    @Override
    public void delete(Long accountId) {
        accountRepository.delete(accountId);
    }
}
