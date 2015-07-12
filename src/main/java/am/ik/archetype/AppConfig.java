package am.ik.archetype;

import am.ik.archetype.domain.model.*;
import am.ik.archetype.domain.model.vo.BirthDate;
import am.ik.archetype.domain.model.vo.Email;
import am.ik.archetype.domain.model.vo.Name;
import am.ik.archetype.domain.model.vo.Password;
import am.ik.archetype.domain.repository.account.AccountRepository;
import am.ik.archetype.domain.repository.credential.CredentialRepository;
import am.ik.archetype.domain.repository.login.FailedLoginAttemptRepository;
import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EntityScan(basePackages = "am.ik.archetype.domain.model")
@EnableScheduling
public class AppConfig {
    @Autowired
    DataSourceProperties dataSourceProperties;

    @Bean
    DataSource dataSource() {
        DataSource dataSource = DataSourceBuilder
                .create(this.dataSourceProperties.getClassLoader())
                .url(this.dataSourceProperties.getUrl())
                .username(this.dataSourceProperties.getUsername())
                .password(this.dataSourceProperties.getPassword())
                .build();
        return new DataSourceSpy(dataSource);
    }

    @Bean
    CommandLineRunner init(AccountRepository accountRepository,
                           CredentialRepository credentialRepository,
                           FailedLoginAttemptRepository failedLoginAttemptRepository) {
        return (args) -> {
            {
                Account account = new Account();
                account.setFirstName(new Name("Toshiaki"));
                account.setLastName(new Name("Maki"));
                account.setEmail(new Email("maki@example.com"));
                account.setBirthDate(new BirthDate(LocalDate.now()));
                account.setRoles(Arrays.asList(Role.USER, Role.ADMIN));
                account.setAccountStatus(AccountStatus.ENABLED);

                Credential credential = new Credential();
                credential.setPassword(Password.raw("password").encode(passwordEncoder(), systemHashAlgorithm()));
                credential.setAccount(account);

                credentialRepository.saveAndFlush(credential);
            }

            {
                Account account = new Account();
                account.setFirstName(new Name("Taro"));
                account.setLastName(new Name("Yamada"));
                account.setEmail(new Email("yamada@example.com"));
                account.setBirthDate(new BirthDate(LocalDate.now()));
                account.setRoles(Arrays.asList(Role.USER, Role.ADMIN));
                account.setAccountStatus(AccountStatus.ENABLED);

                Credential credential = new Credential();
                credential.setPassword(Password.raw("password").encode(passwordEncoder(), systemHashAlgorithm()));
                credential.setAccount(account);

                credentialRepository.saveAndFlush(credential);
            }

            {
                Account account = new Account();
                account.setFirstName(new Name("Jiro"));
                account.setLastName(new Name("Sasaki"));
                account.setEmail(new Email("sasaki@example.com"));
                account.setBirthDate(new BirthDate(LocalDate.now()));
                account.setRoles(Arrays.asList(Role.USER));
                account.setAccountStatus(AccountStatus.ENABLED);

                Credential credential = new Credential();
                credential.setPassword(Password.raw("password").encode(passwordEncoder(), systemHashAlgorithm()));
                credential.setAccount(account);

                credentialRepository.saveAndFlush(credential);
                failedLoginAttemptRepository.save(Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8).map(i -> {
                    FailedLoginAttempt attempt = new FailedLoginAttempt();
                    FailedLoginAttempt.Id id = new FailedLoginAttempt.Id();
                    id.setAccount(account);
                    id.setAttemptTime(new Timestamp(new Date().getTime() + (i * 30_000) - 2 * 60 * 1000));
                    attempt.setAttemptId(id);
                    return attempt;
                }).collect(Collectors.toList()));
                failedLoginAttemptRepository.flush();

            }
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    Password.HashAlgorithm systemHashAlgorithm() {
        return Password.HashAlgorithm.BCRYPT;
    }
}