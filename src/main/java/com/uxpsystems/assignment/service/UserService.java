package com.uxpsystems.assignment.service;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import com.uxpsystems.assignment.dao.IUserRepository;
import com.uxpsystems.assignment.exception.ApplicationRuntimeCustomErrors;
import com.uxpsystems.assignment.model.User;
import com.uxpsystems.assignment.model.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Service("userService")
public class UserService implements IUserService{

    public static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailService emailService;
    /*@Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }*/

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByConfirmationToken(String confirmationToken) {
        return userRepository.findByConfirmationToken(confirmationToken);
    }

    @Override
    public User findByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public User findByUserNameAndPassword(String userName, String password) {
        return userRepository.findByUserNameAndPassword(userName, password);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public Optional<User> getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user;
    }

    @Override
    public String addUser(User user,String appUrl) {

        logger.info("~~~~~~~~~~~~~IN addUser ~~~~~~~~~~~~~");
        // Disable user until the authentication of token is not done
        // by click on confirmation link sent over the registered email
        user.setStatus(UserStatus.DEACTIVATED.getStatus());
        // Generate random 36-character string token for confirmation link
        user.setConfirmationToken(UUID.randomUUID().toString());
        user.setRegDate(new Date());
        logger.info("Adding new User : {}", user);
        userRepository.save(user);
        logger.info("~~~~~~~~~~~~~After saving/Adding the new User : {}", user);
        // Now send the confirmation token over the users registered email:
        String deliveryMsg = sendAuthenticationTokenEmail(user, appUrl);

        logger.info("~~~~~~~~~~~~~After sendAuthenticationTokenEmail ~~~~~~~~~~~~~");
        return deliveryMsg;
    }

    @Override
    public ModelAndView confirmUser(ModelAndView modelAndView,
                                    BindingResult bindingResult,
                                    Map<String, String> requestParams,
                                    RedirectAttributes redir) {
        modelAndView.setViewName("confirmuser");
        Zxcvbn passwordCheck = new Zxcvbn();
        Strength strength = passwordCheck.measure(requestParams.get("password"));
        if (strength.getScore() < 3) {
            //modelAndView.addObject("errorMessage", "Your password is too weak.  Choose a stronger one.");
            bindingResult.reject("password");
            redir.addFlashAttribute("errorMessage", "Your password is too weak.  Choose a stronger one.");
            modelAndView.setViewName("redirect:confirmuser?token=" + requestParams.get("token"));
            logger.error(requestParams.get("token"));
            return modelAndView;
        }
        // Find the user associated with the reset token
        User user = userRepository.findByConfirmationToken(requestParams.get("token"));
        // Set new password
        user.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password")));
        // Set user to ACTIVATED
        user.setStatus(UserStatus.ACTIVATED.getStatus());
        // Save user details
        userRepository.save(user);
        modelAndView.addObject("successMessage", "Your password has been set!");
        return modelAndView;
    }
    @Override
    public void deleteUser(User user) { userRepository.delete(user);
    }
    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
    @Override
    public User findById(Long id) {
        return userRepository.getOne(id);
    }
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    @Override
    public boolean isUserExist(User user) {
        return findByUserName(user.getUserName()) != null;
    }
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
    @Override
    public void updateUser(User user){
        user.setUpdateDate(new Date());
        saveUser(user);
    }

    private String sendAuthenticationTokenEmail(User user, String appUrl) {

        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(user.getEmail());
        registrationEmail.setSubject("Registration Confirmation");
        registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
                + appUrl + "/confirmuser?token=" + user.getConfirmationToken());
        registrationEmail.setFrom("noreply@domain.com");

        emailService.sendEmail(registrationEmail);
        return "success";
    }
}
