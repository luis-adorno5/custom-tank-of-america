package com.codedifferently.tankofamerica.domain.user.services;

import com.codedifferently.tankofamerica.domain.user.exceptions.AccountCreationException;
import com.codedifferently.tankofamerica.domain.user.exceptions.UserNotFoundException;
import com.codedifferently.tankofamerica.domain.user.models.User;

public interface UserService {
    User create(User user) throws AccountCreationException;
    String getAllUsers();
    User getById(Long id) throws UserNotFoundException;
    User getByEmail(String email) throws UserNotFoundException;
    Boolean isEmailUnique(String email);
    User deleteUser(Long id) throws UserNotFoundException;
    User updateUser(User user) throws UserNotFoundException;
}
