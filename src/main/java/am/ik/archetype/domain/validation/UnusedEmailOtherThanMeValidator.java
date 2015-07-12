package am.ik.archetype.domain.validation;

import am.ik.archetype.domain.model.vo.Email;
import am.ik.archetype.domain.service.account.AccountService;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UnusedEmailOtherThanMeValidator implements
        ConstraintValidator<UnusedEmailOtherThanMe, Object> {

    @Autowired
    AccountService accountService;

    private String emailFieldName;
    private String accountIdFieldName;
    private String message;

    @Override
    public void initialize(UnusedEmailOtherThanMe constraintAnnotation) {
        this.accountIdFieldName = constraintAnnotation.accountId();
        this.emailFieldName = constraintAnnotation.email();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        String email = String.class.cast(beanWrapper.getPropertyValue(emailFieldName));
        Long accountId = Long.class.cast(beanWrapper.getPropertyValue(accountIdFieldName));

        if (accountService.isUnusedEmailOtherThanMe(new Email(email), accountId)) {
            return true;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(emailFieldName).addConstraintViolation();
            return false;
        }
    }

}