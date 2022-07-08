package com.codedifferently.tankofamerica.domain.account.controllers;

import com.codedifferently.tankofamerica.domain.account.models.Account;
import com.codedifferently.tankofamerica.domain.account.services.AccountService;
import com.codedifferently.tankofamerica.domain.user.exceptions.UserNotFoundException;
import com.codedifferently.tankofamerica.domain.user.services.UserService;
import com.codedifferently.tankofamerica.domain.utils.LoginHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class AccountController {

    private LoginHelper loginHelper;
    private AccountService accountService;
    private UserService userService;

    @Autowired
    public AccountController(AccountService accountService, UserService userService, LoginHelper userInfo) {
        this.accountService = accountService;
        this.userService = userService;
        this.loginHelper = userInfo;
    }

    @ShellMethod(value = "Sign in to access account functionality provide -E email and -P password", key = "account login")
    public String signIn(@ShellOption({"-E", "--email"}) String username,
                       @ShellOption({"-P", "--password"}) String password){
        try{
            loginHelper.setCurrentUser(userService.getById(2L));
            loginHelper.setSignedIn(true);
            return "Successfully logged in!";
        } catch (UserNotFoundException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Create new Account with args -U user id and -N The name of the account", key = "account new")
    @ShellMethodAvailability("isSignedIn")
    public String createNewAccount (@ShellOption({"-U","--user"}) Long id,
                                     @ShellOption({"-N", "--name"}) String name){
        try {
            Account account = new Account(name);
            account = accountService.create(id, account);
            return account.toString();
        } catch (UserNotFoundException e) {
            return "The User Id is invalid";
        }
    }

    @ShellMethod(value = "Get specified user's accounts with -U user id", key = "account get")
    public String userAccounts(@ShellOption({"-U","--user"}) Long id){

        try{
            return accountService.getAllFromUser(id);
        }catch (UserNotFoundException e){
            return "The User Id is invalid";
        }

    }

    public Availability isSignedIn()
    {
        return loginHelper.getSignedIn() ?
                Availability.available() : Availability.unavailable("Must be signed in first");
    }
}
