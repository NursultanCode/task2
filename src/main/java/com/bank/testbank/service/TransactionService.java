package com.bank.testbank.service;

import com.bank.testbank.config.JwtTokenUtil;
import com.bank.testbank.entity.Account;
import com.bank.testbank.entity.Transaction;
import com.bank.testbank.exception.BadRequestException;
import com.bank.testbank.repository.AccountRepository;
import com.bank.testbank.repository.TransactionRepository;
import com.bank.testbank.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AccountService accountService;

    private final Logger logger = LogManager.getLogger(getClass());


    public String transferMoney(String token) {
        String userName = jwtTokenUtil.getUserNameFromToken(token);

        Account account = accountService.getAccountByAccountNo(userName);
        if (account==null){
            logger.info("Account not found");
            throw new BadRequestException("Account not found");
        }
        if (account.getBalance()<1.1){
            logger.error("Not enough balance in account");
            throw new BadRequestException("Not enough balance");
        }
        return makeTransaction(account);


    }

    private String makeTransaction(Account account) {
        Transaction transaction = new Transaction();
        transaction.setAmount(1.1);
        transaction.setAccount(account);
        transaction.setDate(new Date());
        transactionRepository.save(transaction);
        account.setBalance((BigDecimal.valueOf(account.getBalance()).subtract(new BigDecimal("1.1"))).doubleValue());
        accountService.updateAccount(account);
        return "transaction done; balance is: "+account.getBalance();
    }
}
