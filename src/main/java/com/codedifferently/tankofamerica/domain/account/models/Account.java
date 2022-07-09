package com.codedifferently.tankofamerica.domain.account.models;

import com.codedifferently.tankofamerica.domain.user.models.User;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="accounts")
public class Account {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name= "uuid2", strategy = "uuid2")
    @Type(type="uuid-char")
    private UUID id;
    private String name;
    private Double balance = 0.0;
    @ManyToOne()
    private User owner;
    private Boolean frozen = false;

    public Account() {
    }

    public Account(String name){
        this.name = name;
    }

    public Account(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Boolean getFrozen() {
        return frozen;
    }

    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(name, account.name) && Objects.equals(balance, account.balance) && Objects.equals(owner, account.owner) && Objects.equals(frozen, account.frozen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, balance, owner, frozen);
    }

    public String toString(){
        return String.format("Account for %s named %s with id %s", owner.getFirstName(), name, id.toString());
    }
}
