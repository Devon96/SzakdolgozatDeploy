package hu.utazo.utazo.controller;

import hu.utazo.utazo.model.User;
import hu.utazo.utazo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("title", "Bejelentkezés");
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model, User user) {
        model.addAttribute("title", "Regisztráció");
        return "register";
    }

    @PostMapping("/register")
    public String postRegisterPage(Model model, User user) throws Exception {
        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/register/validation/{code}")
    public String getRegisterPage(@PathVariable String code) {
        userService.validate(code);
        return "redirect:/login?profileValid";
    }


    @GetMapping("/users/list")
    public String usersPage(Model model) {
        model.addAttribute("title", "Felhasználók");
        return "redirect:/users/list/0";
    }

    @GetMapping("/users/list/{pageNumber}")
    public String usersPage(Model model, @PathVariable int pageNumber, @RequestParam(defaultValue = "") String search) {
        model.addAttribute("title", "Felhasználók");
        model.addAttribute("users", userService.findAll(search, pageNumber));
        return "users";
    }

    @GetMapping("/users/ban/{pageNumber}")
    public String banUser(@PathVariable int pageNumber, @RequestParam(defaultValue = "") String search, @RequestParam(defaultValue = "0") long userId) throws Exception {

        userService.negateEnabled(userId);

        String url = "redirect:/users/list/" + pageNumber;
        if( ! search.equals("") ){
            url += "/?search=" + search;
        }
        return url;
    }

    @GetMapping("/users/admin-add-remove/{pageNumber}")
    public String addRemoveAdminRole(@PathVariable int pageNumber, @RequestParam(defaultValue = "") String search, @RequestParam(defaultValue = "0") long userId) throws Exception {

        userService.addRemoveAdminRole(userId);

        String url = "redirect:/users/list/" + pageNumber;
        if( ! search.equals("") ){
            url += "/?search=" + search;
        }
        return url;
    }

    @GetMapping("/user/edit")
    public String editUser(Model model) throws Exception {
        model.addAttribute("user", userService.find());
        return "edit/edit-user";
    }

    @PostMapping("/user/edit")
    public String editUserPost(Model model, User user, RedirectAttributes redirectAttributes) throws Exception {

        if(userService.updateUser(user)){
            SecurityContextHolder.clearContext();
            return "redirect:/login?newEmail";
        }else{
            SecurityContextHolder.clearContext();
            return "redirect:/login?editProfile";
        }


    }


}
