package com.uxpsystems.assignment.service;

import com.uxpsystems.assignment.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService {
    public User findByUserName(String userName);
    public User findByEmail(String email);
    public User findByConfirmationToken(String confirmationToken);
    public User findByFirstName(String firstName);
    public User findByEmailAndPassword(String email, String password);
    public User findByUserNameAndPassword(String userName, String password);

    public ResponseEntity<List<User>> getAllUsers();
    public ResponseEntity<User> getUser(final Long id);
    public ResponseEntity<User> addUser(final User user);
    public ResponseEntity<User> updateUser(final Long id, final User user);
    public ResponseEntity<User> deleteUser(final Long id);
}
