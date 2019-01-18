package com.uxpsystems.assignment.service;

import com.uxpsystems.assignment.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IUserService {
    public User findById(Long id);
    public User findByUserName(String userName);
    public User findByEmail(String email);
    public User findByConfirmationToken(String confirmationToken);
    public User findByFirstName(String firstName);
    public User findByEmailAndPassword(String email, String password);
    public User findByUserNameAndPassword(String userName, String password);

    public List<User> getAllUsers();
    public Optional<User> getUser(final Long id);
    public String addUser(User user,String appUrl) ;
    public void deleteUser(User user);
    public ModelAndView confirmUser(ModelAndView modelAndView, BindingResult bindingResult, Map<String,
            String> requestParams, RedirectAttributes redir);

    public void deleteAllUsers();
    public List<User> findAllUsers();
    public boolean isUserExist(User user);
    public void saveUser(User user);
    public void updateUser(User user);
}
