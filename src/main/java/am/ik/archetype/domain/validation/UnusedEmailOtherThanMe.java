package am.ik.archetype.domain.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Documented
@Constraint(validatedBy = {UnusedEmailOtherThanMeValidator.class})
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface UnusedEmailOtherThanMe {
    String message() default "{am.ik.archetype.domain.validation.UnusedEmailOtherThanMe.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String email() default "email";

    String accountId() default "accountId";

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        UnusedEmailOtherThanMe[] value();
    }
}