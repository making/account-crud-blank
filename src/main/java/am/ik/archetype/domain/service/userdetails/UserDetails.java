package am.ik.archetype.domain.service.userdetails;


import am.ik.archetype.domain.model.Account;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@Builder
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
    private final String username;
    private final String password;
    private final boolean enabled;
    private final boolean accountNonExpired;
    private final boolean credentialsNonExpired;
    private final boolean accountNonLocked;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Account account;
}
