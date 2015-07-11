package am.ik.archetype.domain.service.userdetails;

import am.ik.archetype.domain.model.Account;
import am.ik.archetype.domain.model.AccountStatus;
import am.ik.archetype.domain.repository.account.AccountRepository;
import am.ik.archetype.domain.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    @Transactional(readOnly = true)
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail_value(s)
                .orElseThrow(() -> new UsernameNotFoundException(s + " is not found."));
        boolean enabled = account.getAccountStatus() == AccountStatus.ENABLED || account.getAccountStatus() == AccountStatus.INIT;
        return UserDetails.builder()
                .username(s)
                .password(account.getCredential().getPassword().getValue())
                .enabled(enabled)
                .authorities(account.getRoles().stream()
                        .map(Autority::new)
                        .collect(Collectors.toList()))
                .accountNonLocked(!accountService.isLocked(account))
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .account(account)
                .build();
    }
}
