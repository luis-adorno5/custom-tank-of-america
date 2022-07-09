package com.codedifferently.tankofamerica.domain.account.services;

import com.codedifferently.tankofamerica.domain.account.exceptions.AccountNotFoundException;
import com.codedifferently.tankofamerica.domain.account.models.Account;
import com.codedifferently.tankofamerica.domain.user.exceptions.UserNotFoundException;
import com.codedifferently.tankofamerica.domain.user.models.User;

import java.util.Optional;

public interface AccountService {
    Account create(Long userId, Account account) throws UserNotFoundException;
    String getById(String id);
    Account getByName(String name, User owner) throws AccountNotFoundException;
    String getAllFromUser(Long userId) throws UserNotFoundException;
    Boolean isAccountNameUnique(String name, User owner);
    String updateBalance(Double balance, Account account);
    Account update(Account account);
    Boolean delete(String id);
}
