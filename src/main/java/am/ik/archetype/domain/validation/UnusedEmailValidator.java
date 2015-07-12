package am.ik.archetype.domain.validation;

import am.ik.archetype.domain.model.vo.Email;
import am.ik.archetype.domain.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UnusedEmailValidator implements
        ConstraintValidator<UnusedEmail, String> {

    @Autowired
    AccountService accountService;

    @Override
    public void initialize(UnusedEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return accountService.isUnusedEmail(new Email(value));
    }

}