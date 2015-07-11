package am.ik.archetype.domain.service.userdetails;

import am.ik.archetype.domain.model.Account;
import am.ik.archetype.domain.model.AccountStatus;
import am.ik.archetype.domain.model.Credential;
import am.ik.archetype.domain.repository.credential.CredentialRepository;
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
    CredentialRepository credentialRepository;

    @Transactional(readOnly = true)
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Credential credential = credentialRepository.findByAccount_email_value(s)
                .orElseThrow(() -> new UsernameNotFoundException(s + " is not found."));
        Account account = credential.getAccount();
        boolean enabled = account.getAccountStatus() == AccountStatus.ENABLED || account.getAccountStatus() == AccountStatus.INIT;
        return UserDetails.builder()
                .username(s)
                .password(credential.getPassword().getValue())
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
