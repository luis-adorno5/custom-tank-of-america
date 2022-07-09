package com.codedifferently.tankofamerica.domain.account.controllers;

import com.codedifferently.tankofamerica.domain.account.exceptions.AccountNotFoundException;
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
    public String createNewAccount (@ShellOption({"-N", "--name"}) String name){
        try {
            Account account = new Account(name);
            account = accountService.create(loginHelper.getCurrentUser().getId(), account);
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

    @ShellMethod(value = "Deposit funds to specified account with arg -N name", key = "account deposit")
    @ShellMethodAvailability("isSignedIn")
    public String deposit(@ShellOption({"-N", "--name"}) String name,
                          @ShellOption({"-A", "--amount"}) Double amount){
        if(accountService.isAccountNameUnique(name, loginHelper.getCurrentUser())){
            return "You do not have an account with the specified name!";
        }
        if(amount < 0)
            return "Your deposit amount is invalid!";
        try {
            Account account = accountService.getByName(name, loginHelper.getCurrentUser());
            Double newBalance = account.getBalance() + amount;
            accountService.updateBalance(newBalance, account);
            return String.format("Your new account balance is %.2f", newBalance);
        }catch (AccountNotFoundException e){
            return e.getMessage();
        }
    }

    public Availability isSignedIn()
    {
        return loginHelper.getSignedIn() ?
                Availability.available() : Availability.unavailable("Must be signed in first");
    }
}
