package de.agdb.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Riva on 21.03.2017.
 */
@RestController
@RequestMapping("/Callback")
public class GoogleAuthController {

    @RequestMapping(method = RequestMethod.GET)
    public String getGoogleLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) {
// Below guy should be autowired if you want to use Spring.
        //GoogleAuthorization helper = new GoogleAuthorization();
        System.out.println("angekommen");

        if (request.getParameter("code") == null
                || request.getParameter("state") == null) {

            //model.addAttribute("URL", helper.buildLoginUrl());
            //session.setAttribute("state", helper.getStateToken());

        } else if (request.getParameter("code") != null && request.getParameter("state") != null && request.getParameter("state").equals(session.getAttribute("state"))) {
            session.removeAttribute("state");


                //helper.saveCredentials(request.getParameter("code"));
                return "redirect:/dashboard";

        }
        return "newjsp";
    }

    @RequestMapping(method = RequestMethod.POST  )

    public String sample()
    {
        System.out.println("angekommen");
        return "home";
    }
}
