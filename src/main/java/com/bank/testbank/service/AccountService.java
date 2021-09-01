package com.bank.testbank.service;

import com.bank.testbank.entity.Account;
import com.bank.testbank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account getAccountByAccountNo(String userName) {
        return accountRepository.findAccountByUser_ContactNo(userName);
    }

    public void updateAccount(Account account) {
        accountRepository.save(account);
    }
}
