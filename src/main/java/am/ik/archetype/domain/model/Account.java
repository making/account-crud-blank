package am.ik.archetype.domain.model;

import am.ik.archetype.infra.account.AccountStatusAttributeConverter;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(indexes = {
        @Index(columnList = "email", unique = true),
        @Index(columnList = "accountStatus")
})
@Data
@ToString(exclude = "failedLoginAttempts")
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    @Embedded
    @AttributeOverride(column = @Column(name = "first_name"), name = "value")
    @NotNull
    @Valid
    private Name firstName;
    @Embedded
    @AttributeOverride(column = @Column(name = "last_name"), name = "value")
    @NotNull
    @Valid
    private Name lastName;
    @Embedded
    @AttributeOverride(column = @Column(name = "email"), name = "value")
    @Column(unique = true)
    @NotNull
    @Valid
    private Email email;
    @Embedded
    @AttributeOverride(column = @Column(name = "birth_date"), name = "value")
    @NotNull
    @Valid
    private BirthDate birthDate;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @NotEmpty
    private List<Role> roles;
    @NotNull
    @Convert(converter = AccountStatusAttributeConverter.class)
    private AccountStatus accountStatus;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @NotNull
    @JoinColumn(name = "credential_id")
    private Credential credential;

    @OneToMany(mappedBy = "attemptId.account", cascade = CascadeType.ALL)
    private List<FailedLoginAttempt> failedLoginAttempts;

    @Version
    private Long version;
}
