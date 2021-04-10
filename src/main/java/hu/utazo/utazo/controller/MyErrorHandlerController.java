package hu.utazo.utazo.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MyErrorHandlerController implements ErrorController {

    @RequestMapping("/error")
    public String myErrorHandler(Model model, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI).equals("/oauth_login")){
            return "redirect:/login?error";
        }
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("message", "Az oldal nem található" );
            } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("message", "Szerverhiba" );
            } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("message", "Hozzáférés megtagadva" );
            }else if(statusCode == HttpStatus.BAD_REQUEST.value()) {
                model.addAttribute("message", "Érvénytelen szintaxis" );
            }else {
                model.addAttribute("message", "Ismeretlen hiba" );
            }
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
