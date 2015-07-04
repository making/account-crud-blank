package am.ik.archetype.domain.aspect.auditlog;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Audit {

    String domain() default "";

    String action() default "";

}
