package am.ik.archetype.app.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    @RequestMapping("/loginForm")
    String loginForm() {
        return "loginForm";
    }
}
