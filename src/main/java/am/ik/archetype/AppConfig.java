package am.ik.archetype;

import am.ik.archetype.domain.model.*;
import am.ik.archetype.domain.repository.account.AccountRepository;
import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.Arrays;

@Configuration
@EntityScan(basePackages = "am.ik.archetype.domain.model")
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
    CommandLineRunner init(AccountRepository accountRepository) {
        return (args) -> {
            Account account = new Account();
            account.setFirstName(new Name("Taro"));
            account.setLastName(new Name("Yamada"));
            account.setPassword(Password.raw("pass").encode(passwordEncoder(), systemHashAlgorithm()));
            account.setEmail(new Email("yamada@example.com"));
            account.setBirthDate(new BirthDate(LocalDate.now()));
            account.setRoles(Arrays.asList(Role.USER, Role.ADMIN));
            account.setAccountState(AccountState.ENABLED);
            accountRepository.saveAndFlush(account);
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