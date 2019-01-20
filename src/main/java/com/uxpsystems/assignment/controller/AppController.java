package com.uxpsystems.assignment.controller;

import com.uxpsystems.assignment.dao.IUserRepository;
import com.uxpsystems.assignment.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AppController {
    @Autowired
    private UserService userService;
    @Autowired
    Environment environment;

    @ApiOperation(value = "This will take you to HOME page", response = String.class)
    @RequestMapping(value={"/", "/welcome", "/home","/assignement"}, method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public String showHomePage(Model model, @RequestParam(defaultValue = "0") int page  ){
        model.addAttribute("users", userService.findAllUsers()/*new PageRequest(page, 3)*/);
        //model.addAttribute("currentPage", page);
        return environment.getProperty("app.redirect.user.view.home");
    }
}