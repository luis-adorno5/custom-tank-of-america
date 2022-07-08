package com.codedifferently.tankofamerica.domain.user.controllers;

import com.codedifferently.tankofamerica.domain.user.exceptions.AccountCreationException;
import com.codedifferently.tankofamerica.domain.user.exceptions.UserNotFoundException;
import com.codedifferently.tankofamerica.domain.user.models.User;
import com.codedifferently.tankofamerica.domain.user.services.UserService;
import com.codedifferently.tankofamerica.domain.utils.LoginHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class UserController {
    private LoginHelper loginHelper;
    private UserService userService;

    @Autowired
    public UserController(UserService userService, LoginHelper userInfo) {
        this.userService = userService;
        this.loginHelper = userInfo;
    }

    @ShellMethod(value = "Sign in to access account functionality provide -E email and -P password", key = "user login")
    public String signIn(@ShellOption({"-E", "--email"}) String email,
                         @ShellOption({"-P", "--password"}) String password){
        try {
            User user = userService.getByEmail(email);
            if (isPasswordValid(user, password)) {
                loginHelper.setCurrentUser(user);
                loginHelper.setSignedIn(true);
                return "Successfully logged in.";
            }
            else{
                return "User does not exist.";
            }
        } catch (UserNotFoundException e) {
            return e.getMessage();
        }
    }

    private Boolean isPasswordValid(User user, String password){
        return user.getPassword().equals(password);
    }

    @ShellMethod(value = "Create a new User: -F first name -L last name, -E email, -P password", key = "user signup")
    public String signUp(@ShellOption({"-F", "--firstname"}) String firstName,
                              @ShellOption({"-L", "--lastname"})String lastName,
                              @ShellOption({"-E", "--email"})String email,
                              @ShellOption({"-P", "--password"})String password){
        try {
            if (userService.isEmailUnique(email)) {
                User user = new User(firstName, lastName, email, password);
                user = userService.create(user);
                loginHelper.setCurrentUser(user);
                loginHelper.setSignedIn(true);
                return "You have successfully signed up";
            }
        }catch (AccountCreationException e){
            return e.getMessage();
        }
        return "The email you entered is already registered to a user!";
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

    @ShellMethod(value = "Update a user's information.", key = "user update")
    @ShellMethodAvailability("isSignedIn")
    public String updateUser(@ShellOption({"-U", "--userId"}) Long id,
                             @ShellOption({"-F", "--firstname"}) String firstName,
                             @ShellOption({"-L", "--lastname"}) String lastName,
                             @ShellOption({"-E", "--email"}) String email,
                             @ShellOption({"-P", "--password"}) String password) throws UserNotFoundException {
        if(firstName.length() == 0 || lastName.length() == 0 || email.length() == 0 || password.length() == 0)
            return String.format("You did not provide changes to all of the fields.");
        User user = setUserInfo(id, firstName, lastName, email, password);
        userService.updateUser(user);
        return String.format("Changed information of %s", user);
    }

    private User setUserInfo(Long id, String firstName, String lastName, String email, String password){
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

    public Availability isSignedIn()
    {
        return loginHelper.getSignedIn() ?
                Availability.available() : Availability.unavailable("Must be signed in first");
    }

}
