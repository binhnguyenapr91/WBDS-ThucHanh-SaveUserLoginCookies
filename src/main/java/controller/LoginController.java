package controller;

import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes("user")
public class LoginController {
    @ModelAttribute("user")
    public User getUserForm() {
        return new User();
    }

    @GetMapping("/login")
    public String showLoginForm(@CookieValue(value = "setUser", defaultValue = "") String setUser, Model model) {
        Cookie cookie = new Cookie("setUser", setUser);
        model.addAttribute("cookieValue", cookie);
        return "login";
    }

    @PostMapping("/do-login")
    public String doLogin(@ModelAttribute("user") User user, Model model, @CookieValue(value = "setUser", defaultValue = "") String setUser,
                          HttpServletRequest request, HttpServletResponse response) {
        if (user.getUsername().equals("admin") && user.getPassword().equals("12345")) {
            if (user.getUsername() != null) {
                setUser = user.getUsername();
            }
            Cookie cookie = new Cookie("setUser", setUser);
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);

            Cookie[] cookies = request.getCookies();

            for (Cookie ck : cookies) {
                if (ck.getName().equals("setUser")) {
                    model.addAttribute("cookieValue",ck);
                    break;
                }else {
                    ck.setValue("");
                    model.addAttribute("cookieValue",ck);
                    break;
                }
            }
            model.addAttribute("message","Login successfully! Welcome");
        }else{
            user.setUsername("");
            Cookie cookie = new Cookie("setUser",setUser);
            model.addAttribute("message","Login fail");
            model.addAttribute("cookieValue",cookie);
        }
        return "login";
    }
}
