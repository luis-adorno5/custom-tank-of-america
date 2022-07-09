package com.codedifferently.tankofamerica.domain.account.exceptions;

public class AccountNotFoundException extends Exception{
    public AccountNotFoundException(String message) {
        super(message);
    }
}
