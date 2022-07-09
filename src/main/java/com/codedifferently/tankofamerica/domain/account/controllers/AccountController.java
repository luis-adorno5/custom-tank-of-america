package com.codedifferently.tankofamerica.domain.account.controllers;

import com.codedifferently.tankofamerica.domain.account.models.Account;
import com.codedifferently.tankofamerica.domain.account.services.AccountService;
import com.codedifferently.tankofamerica.domain.user.exceptions.UserNotFoundException;
import com.codedifferently.tankofamerica.domain.user.models.UserRoles;
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

    @ShellMethod(value = "Create new Account with the argument -N The name of the account", key = "account new")
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

    @ShellMethod(value = "Get specified user's accounts.", key = "account get all")
    @ShellMethodAvailability("isSignedIn")
    public String userAccounts(){

        try{
            return accountService.getAllFromUser(loginHelper.getCurrentUser().getId());
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
