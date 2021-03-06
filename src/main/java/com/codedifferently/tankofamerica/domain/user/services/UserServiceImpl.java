package com.codedifferently.tankofamerica.domain.user.services;

import com.codedifferently.tankofamerica.domain.user.exceptions.AccountCreationException;
import com.codedifferently.tankofamerica.domain.user.exceptions.UserNotFoundException;
import com.codedifferently.tankofamerica.domain.user.models.User;
import com.codedifferently.tankofamerica.domain.user.models.UserRoles;
import com.codedifferently.tankofamerica.domain.user.repos.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User create(User user) throws AccountCreationException {
        if(isEmailUnique(user.getEmail())) {
            user.setRole(UserRoles.USER);
            return userRepo.save(user);
        }
        throw new AccountCreationException();
    }

    public String getAllUsers(){
        StringBuilder builder = new StringBuilder();
        Iterable<User> users = userRepo.findAll();
        for (User user:users) {
            builder.append(user.toString() +"\n");
        }
        return builder.toString().trim();
    }

    @Override
    public User getById(Long id) throws UserNotFoundException {
        Optional<User> optional = userRepo.findById(id);
        if(optional.isEmpty())
            throw new UserNotFoundException(String.format("User with id {%s} not found", id));
        return optional.get();
    }

    public User getByEmail(String email) throws UserNotFoundException{
        Optional<User> optional = userRepo.findByEmail(email);
        if(optional.isEmpty())
            throw new UserNotFoundException(String.format("User with email {%s} not found", email));
        return optional.get();
    }

    @Override
    public Boolean isEmailUnique(String email) {
        Optional<User> optional = userRepo.findByEmail(email);
        return optional.isEmpty();
    }

    public User deleteUser(Long id) throws UserNotFoundException {
        Optional<User> optional = userRepo.findById(id);
        if(optional.isEmpty())
            throw new UserNotFoundException(String.format("User with id {%s} not found", id));
        userRepo.delete(optional.get());
        return optional.get();
    }

    public User updateUser(User user) throws UserNotFoundException{
        if(user == null)
            throw new UserNotFoundException(String.format("No user info was provided."));
        userRepo.save(user);
        return user;
    }

}
