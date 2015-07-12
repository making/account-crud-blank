package am.ik.archetype.domain.repository.credential;

import am.ik.archetype.domain.model.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
    Optional<Credential> findByAccount_email_value(String email);

    int deleteByAccount_accountId(Long accountId);
}
