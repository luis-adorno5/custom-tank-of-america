package com.codedifferently.tankofamerica.domain.user.controllers;

import com.codedifferently.tankofamerica.domain.user.exceptions.UserNotFoundException;
import com.codedifferently.tankofamerica.domain.user.models.User;
import com.codedifferently.tankofamerica.domain.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ShellMethod(value = "Create a new User: -F first name -L last name, -E email, -P password", key = "user new")
    public User createNewUser(@ShellOption({"-F", "--firstname"}) String firstName,
                              @ShellOption({"-L", "--lastname"})String lastName,
                              @ShellOption({"-E", "--email"})String email,
                              @ShellOption({"-P", "--password"})String password){
        User user = new User(firstName,lastName,email,password);
        user = userService.create(user);
        return user;

    }

    @ShellMethod(value = "Get All Users", key = "user get all")
    public String getAllUsers(){
        return userService.getAllUsers();
    }

    @ShellMethod(value = "Remove a user given an id: -U userId", key = "user delete")
    public String removeUser(@ShellOption({"-U", "--userId"}) Long id) throws UserNotFoundException {
        User user = userService.deleteUser(id);
        return String.format("User %s was deleted.", user.toString());
    }
}
