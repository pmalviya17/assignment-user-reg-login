package com.uxpsystems.assignment.controller;

import com.uxpsystems.assignment.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AppController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "This will take you to HOME page",
            response = String.class)
    @RequestMapping(value={"/", "/welcome", "/home","/assignement"}, method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public String showHomePage(Model model){
        model.addAttribute("users", userService.findAllUsers());
        return "assignement";
    }
}