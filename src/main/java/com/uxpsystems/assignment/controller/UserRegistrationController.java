package com.uxpsystems.assignment.controller;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import com.uxpsystems.assignment.exception.ApplicationRuntimeCustomErrors;
import com.uxpsystems.assignment.model.User;
import com.uxpsystems.assignment.model.UserStatus;
import com.uxpsystems.assignment.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Api(value="amdocsuserregistration", description="Amdocs-User registration page")
public class UserRegistrationController {

    public static final Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);

    @Autowired
    Environment environment;

    private UserService userService;

    @Autowired
    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    // Return registration form template
    @ApiOperation(value = "To Show/Load user registration page ",response = ModelAndView.class)
    @RequestMapping(value="/user/add", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelAndView showRegistrationPage(ModelAndView modelAndView, User user){
        modelAndView.addObject("user", user);
        modelAndView.setViewName(environment.getProperty("app.redirect.user.view.add"));
        return modelAndView;
    }

    @ApiOperation(value = "Input & Submit User Registration page ",response = ModelAndView.class)
    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public ModelAndView processRegistrationForm(
            ModelAndView modelAndView, @Valid User user, BindingResult bindingResult,
            HttpServletRequest request) {

        if (userService.findByUserName(user.getUserName()) != null  ) {
            logger.error("Unable to create. A User with username {} already exist", user.getUserName());
            modelAndView.addObject("alreadyRegisteredMessage", "Oops!  There is already a user registered with the same username provided.");
            modelAndView.setViewName(environment.getProperty("app.redirect.user.view.add"));
            bindingResult.reject("userName");
            return modelAndView;
        }else if (userService.findByEmail(user.getEmail())!= null ) {
            logger.error("Unable to create. A User with Email {} already exist", user.getEmail());
            modelAndView.addObject("alreadyRegisteredMessage", "Oops!  There is already a user registered with the email provided.");
            modelAndView.setViewName(environment.getProperty("app.redirect.user.view.add"));
            bindingResult.reject("email");
            return modelAndView;
        }

        String appUrl = request.getScheme() + "://" + request.getServerName();
        String deliveryMsg =  userService.addUser(user, appUrl);
        modelAndView.addObject("confirmationMessage", "A confirmation e-mail has been sent to " + user.getEmail() + " " +deliveryMsg+
                "\nPlease click on the provided link to activate the User :"+user.getUserName());
        modelAndView.setViewName(environment.getProperty("app.redirect.user.view.home"));
        modelAndView.addObject("users", userService.findAllUsers());
        return modelAndView;
    }

    @ApiOperation(value = "To Show/Load User confirmation page where valid password can be set against there entry ",response = ModelAndView.class)
    @RequestMapping(value="/user/confirm", method = RequestMethod.GET)
    public ModelAndView showConfirmRegistration(ModelAndView modelAndView, @RequestParam("token") String token) {
        User user = userService.findByConfirmationToken(token);
        if (user == null) { // No token found in DB
            modelAndView.addObject("invalidToken", "Oops!  This is an invalid confirmation link.");
        } else { // Token found
            modelAndView.addObject("confirmationToken", user.getConfirmationToken());
        }
        modelAndView.setViewName(environment.getProperty("app.redirect.user.view.confirm"));
        return modelAndView;
    }

    @ApiOperation(value = "Enter strong passwords for User confirmation page where valid password can be set against there entry ",response = ModelAndView.class)
    @RequestMapping(value="/user/confirm", method = RequestMethod.POST)
    public ModelAndView confirmRegistration(ModelAndView modelAndView, BindingResult bindingResult, @RequestParam Map<String,
            String> requestParams, RedirectAttributes redir) {
        Zxcvbn passwordCheck = new Zxcvbn();
        Strength strength = passwordCheck.measure(requestParams.get("password"));
        if (strength.getScore() < 3) {
            //modelAndView.addObject("errorMessage", "Your password is too weak.  Choose a stronger one.");
            bindingResult.reject("password");
            redir.addFlashAttribute("errorMessage", "Your password is too weak.  Choose a stronger one.");
            modelAndView.setViewName("redirect:/user/confirm?token=" + requestParams.get("token"));
            logger.error(requestParams.get("token"));
            return modelAndView;
        }
        // Save user details
        userService.confirmUser(requestParams);
        modelAndView.addObject("confirmationMessage", "Your password has been set & User is now activated!");
        modelAndView.setViewName(environment.getProperty("app.redirect.user.view.home"));
        modelAndView.addObject("users", userService.findAllUsers());
        return modelAndView;
    }

    @ApiOperation(value = "To Show/Load User Update/Edit page",response = ModelAndView.class)
    @RequestMapping(value="/user/update", method = RequestMethod.GET)
    public ModelAndView showUpdateForm(ModelAndView modelAndView,
                                            @RequestParam("userName") String userName) {
        logger.info("~~~~~~~~~~~~~showUpdateForm 1 : IN");

        User user = userService.findByUserName(userName);
        if (user == null) { // No token found in DB
            modelAndView.addObject("invalidID", "Oops!  This is an invalid confirmation link.");
        } else { // Token found
            modelAndView.addObject("userName", user.getUserName());
        }
        modelAndView.addObject("user", user);
        modelAndView.setViewName(environment.getProperty("app.redirect.user.view.update"));
        logger.info("~~~~~~~~~~~~~showUpdateForm 1 : OUT");
        return modelAndView;
    }

    @ApiOperation(value = "Submit User Updated/Edited detail",response = ModelAndView.class)
    @RequestMapping(value="/user/update", method = RequestMethod.POST)
    public ModelAndView updateForm(ModelAndView modelAndView, @Valid User userForm,
                                       BindingResult bindingResult,
                                       @RequestParam Map<String,
            String> requestParams, RedirectAttributes redir) {
        logger.info("~~~~~~~~~~~~~updateForm : IN");
        // Update user details
        User userOrg  = userService.findByUserName((requestParams.get("userName")));
        if (userOrg==null){
            bindingResult.reject("userName");
            redir.addFlashAttribute("errorMessage", "Invalid user userName:" + requestParams.get("userName"));
            modelAndView.setViewName("redirect:/"
                    +environment.getProperty("app.redirect.user.view.update")+"?userName=" + requestParams.get("userName"));
        } else {
            userOrg.setFirstName(userForm.getFirstName());
            userOrg.setLastName(userForm.getLastName());
            userOrg.setEmail(userForm.getEmail());
            userService.updateUser(userOrg);
            modelAndView.setViewName(environment.getProperty("app.redirect.user.view.home"));
            modelAndView.addObject("successMessage", "User has been Updated!");
            modelAndView.addObject("users", userService.findAllUsers());
        }
        logger.info("~~~~~~~~~~~~~ updateForm : OUT");
        return modelAndView;
    }

    @ApiOperation(value = "Delete User from the list (Only do it with Admin rights)",response = ModelAndView.class)
    @RequestMapping(value = "/user/delete")
    public ModelAndView deleteUser(@RequestParam("id") long id, ModelAndView modelAndView, @Valid User userForm, BindingResult bindingResult) {
        logger.info("~~~~~~~~~~~~~  deleteUser  : IN");
        User user = userService.findUserById(userForm.getId()).get();
        logger.info("user1--------------"+user);
        if (user==null)new IllegalArgumentException("Invalid user Id:" + id);
        userService.deleteUser(user);
        modelAndView.addObject("users", userService.findAllUsers());
        modelAndView.setViewName(environment.getProperty("app.redirect.user.view.delete"));
        modelAndView.addObject("confirmationMessage", "User \""+user.getUserName()+"\" deleted successfully now !");
        logger.info("~~~~~~~~~~~~~  deleteUser  : OUT");
        return modelAndView;
    }

    @ApiOperation(value = "Deactivate User from the list (Only do it with Admin rights)",response = ModelAndView.class)
    @RequestMapping(value = "/user/deactivate")
    public ModelAndView deactivateUser(@RequestParam("id") long id, ModelAndView modelAndView, @Valid User userForm,
                                 BindingResult bindingResult) {
       logger.info("~~~~~ IN deactivateUser ~~~~~");
        User user = userService.findUserById(userForm.getId()).get();
        if (user==null)new IllegalArgumentException("Invalid user Id:" + id);
        user.setStatus(UserStatus.DEACTIVATED.getStatus());
        userService.updateUser(user);
        modelAndView.addObject("users", userService.findAllUsers());
        modelAndView.setViewName(environment.getProperty("app.redirect.user.view.deactivate"));
        modelAndView.addObject("confirmationMessage", "User \""+user.getUserName()+"\" Updated successfully now !");
        logger.info("~~~~~~~~~~~~~  deactivateUser  : OUT");
        return modelAndView;
    }

    @ApiOperation(value = "Get All Users list",response = ResponseEntity.class)
    @RequestMapping(value="/user/all", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()){
            return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @ApiOperation(value = "Search User with an ID",response = ResponseEntity.class)
    @RequestMapping(value="/show/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserById(@PathVariable("id") final Long id) {
        Optional<User> user = userService.findUserById(id);
        return user.map(User -> new ResponseEntity<>(User, HttpStatus.OK)).
                orElseGet(() -> new ResponseEntity<>(
                        new ApplicationRuntimeCustomErrors("User record with this id "
                                + id + " not found"), HttpStatus.NOT_FOUND));
    }
}
