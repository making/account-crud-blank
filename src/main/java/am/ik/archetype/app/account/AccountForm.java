package am.ik.archetype.app.account;

import am.ik.archetype.domain.model.*;
import am.ik.archetype.domain.validation.Confirm;
import am.ik.archetype.domain.validation.UnusedEmailOtherThanMe;
import am.ik.archetype.domain.validation.groups.CrudMode;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.List;

@Data
@Confirm(field = "password.value")
@UnusedEmailOtherThanMe(groups = CrudMode.Update.class, email = "email.value")
public class AccountForm implements Serializable {
    @NotNull(groups = {CrudMode.Update.class, CrudMode.Delete.class})
    private Long accountId;
    @NotNull
    @Valid
    private Name firstName;
    @NotNull
    @Valid
    private Name lastName;
    @NotNull
    @Valid
    private Email email;
    @NotNull
    @Valid
    private BirthDate birthDate;
    @NotNull
    @Valid
    private Password password;
    @NotNull(groups = CrudMode.Create.class)
    @Valid
    private Password confirmPassword;
    @NotEmpty
    private List<Role> roles;
    @NotNull(groups = CrudMode.Update.class)
    @Null(groups = CrudMode.Create.class)
    private AccountState accountState;

    private boolean locked;
}
