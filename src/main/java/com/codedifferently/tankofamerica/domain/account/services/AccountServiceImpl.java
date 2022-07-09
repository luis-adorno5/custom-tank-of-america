package com.codedifferently.tankofamerica.domain.account.services;

import com.codedifferently.tankofamerica.domain.account.exceptions.AccountNotFoundException;
import com.codedifferently.tankofamerica.domain.account.models.Account;
import com.codedifferently.tankofamerica.domain.account.repo.AccountRepo;
import com.codedifferently.tankofamerica.domain.user.exceptions.UserNotFoundException;
import com.codedifferently.tankofamerica.domain.user.models.User;
import com.codedifferently.tankofamerica.domain.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService{

    private AccountRepo accountRepo;
    private UserService userService;

    @Autowired
    public AccountServiceImpl(AccountRepo accountRepo, UserService userService) {
        this.accountRepo = accountRepo;
        this.userService = userService;
    }

    @Override
    public Account create(Long userId, Account account) throws UserNotFoundException {
        User owner = userService.getById(userId);
        account.setOwner(owner);
        return accountRepo.save(account);
    }

    @Override
    public String getById(String id) {
        return null;
    }

    @Override
    public Account getByName(String name, User owner) throws AccountNotFoundException{
        List<Account> accounts = accountRepo.findByOwner(owner);
        for(Account account : accounts){
            if(account.getName().equals(name))
                return account;
        }
        throw new AccountNotFoundException(String.format("You do not own an account with name %s", name));
    }

    @Override
    public String getAllFromUser(Long userId) throws UserNotFoundException {
        StringBuilder builder = new StringBuilder();
        User owner = userService.getById(userId);
        List<Account> accounts = accountRepo.findByOwner(owner);
        for (Account account: accounts) {
            builder.append(account.toString() + "\n");
        }
        return builder.toString().trim();
    }

    @Override
    public Boolean isAccountNameUnique(String name, User owner) {
        List<Account> accounts = accountRepo.findByOwner(owner);
        for(Account a : accounts){
            if(a.getName().equals(name))
                return false;
        }
        return true;
    }

    @Override
    public String updateBalance(Double balance, Account account) {
        account.setBalance(balance);
        accountRepo.save(account);
        return "Successfully updated the accounts balance.";
    }

    @Override
    public Account update(Account account) {
        return null;
    }

    @Override
    public Boolean delete(String id) {
        return null;
    }
}
