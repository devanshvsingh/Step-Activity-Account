package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InvalidAmountException;

public interface IAccount
{
    int get_account_number();

    String get_customer_name();

    double get_balance();

    String get_account_type();

    String get_status();

    void deposit(double amount) throws InvalidAmountException;

    void withdraw(double amount, String pin) throws AccountException;

    void displayAccountInfo();
}