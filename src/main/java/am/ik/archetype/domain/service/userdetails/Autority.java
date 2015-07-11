package am.ik.archetype.domain.service.userdetails;

import am.ik.archetype.domain.model.Role;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class Autority implements GrantedAuthority {
    private final Role role;

    @Override
    public String getAuthority() {
        return role.name();
    }
}
