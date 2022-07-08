package com.codedifferently.tankofamerica.domain.user.exceptions;

public class AccountCreationException extends Exception{
    public AccountCreationException() {
        super("There is already a user associated with that email.");
    }
}
