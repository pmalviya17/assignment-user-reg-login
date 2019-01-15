package com.uxpsystems.assignment.dao;

import com.uxpsystems.assignment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface IUserRepository extends JpaRepository<User, Long> {
    public User findByUserName(String userName);
    public User findByEmail(String email);
    public User findByConfirmationToken(String confirmationToken);
    public User findByFirstName(String firstName);
    public User findByEmailAndPassword(String email, String password);
    public User findByUserNameAndPassword(String userName, String password);

}

