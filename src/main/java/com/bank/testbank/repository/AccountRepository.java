package com.bank.testbank.repository;

import com.bank.testbank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByUser_ContactNo(String contactNo);
}
