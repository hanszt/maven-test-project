package org.hzt.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer {

    private final String id;
    private final String name;
    private final List<BankAccount> bankAccounts;

    public Customer(String id, String name) {
        this(id, name, new ArrayList<>());
    }
    public Customer(String id, String name, List<BankAccount> bankAccounts) {
        this.id = id;
        this.name = name;
        this.bankAccounts = Collections.unmodifiableList(bankAccounts);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }
}
