package com.uxpsystems.assignment.service;

import com.uxpsystems.assignment.dao.IUserRepository;
import com.uxpsystems.assignment.exception.ApplicationRuntimeCustomErrors;
import com.uxpsystems.assignment.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("userService")
public class UserService implements IUserService{

    public static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private IUserRepository userRepository;
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
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()){
            return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> getUser(Long id) {
        Optional<User> user = userRepository.findById(id);

        return user.map(User -> new ResponseEntity<>(User, HttpStatus.OK)).
                orElseGet(() -> new ResponseEntity<>(new ApplicationRuntimeCustomErrors("User record with this id "
                        + id + " not found"), HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<User> addUser(User user) {
        if (userRepository.findByFirstName(user.getUserName()) != null) {
            logger.error("Unable to create. A User with username {} already exist", user.getUserName());
            return new ResponseEntity<User>
                    (new ApplicationRuntimeCustomErrors("New User not created due to errors as, User with this username "
                            + user.getUserName()+ " already exist."), HttpStatus.CONFLICT);
        }
        userRepository.save(user);
        logger.info("Adding new User : {}", user);
        userRepository.save(user);
        logger.info("After saving the new User : {}", user);
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<User> updateUser(Long id, User user) {
        return null;
    }

    @Override
    public ResponseEntity<User> deleteUser(Long id) {
        return null;
    }
}
