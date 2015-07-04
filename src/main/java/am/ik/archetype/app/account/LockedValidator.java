package am.ik.archetype.app.account;

import am.ik.archetype.domain.model.Account;
import am.ik.archetype.domain.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LockedValidator implements Validator {
    @Autowired
    AccountService accountService;

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountForm form = AccountForm.class.cast(target);
        System.out.println(form);
        if (form.isLocked() && !accountService.isLocked(accountService.findOne(form.getAccountId()))) {
            errors.rejectValue("locked", "AccountForm.locked.LockedValidator", "Cannot lock manually");
        }
    }
}
