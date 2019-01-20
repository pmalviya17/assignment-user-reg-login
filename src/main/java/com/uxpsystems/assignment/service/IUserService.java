package com.uxpsystems.assignment.service;

import com.uxpsystems.assignment.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IUserService {


    public String addUser(User user,String appUrl) ;
    public void deleteUser(User user);
    public void confirmUser(Map<String, String> requestParams);
    public void saveUser(User user);
    public void updateUser(User user);

    public List<User> findAllUsers();
    public Optional<User> findUserById(final Long id);
    public User findByUserName(String userName);
    public User findByEmail(String email);
    public User findByConfirmationToken(String confirmationToken);


    public boolean isUserExist(User user);
    public void deleteAllUsers();
    public User findByFirstName(String firstName);
    public User findByEmailAndPassword(String email, String password);
    public User findByUserNameAndPassword(String userName, String password);
}
