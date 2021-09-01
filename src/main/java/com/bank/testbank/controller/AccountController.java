package com.bank.testbank.controller;

import com.bank.testbank.dto.Response;
import com.bank.testbank.entity.Account;
import com.bank.testbank.exception.BadRequestException;
import com.bank.testbank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AccountController {
    @Autowired
    private TransactionService transactionService;
    @PostMapping("/payment")
    public ResponseEntity<Response> getMoney(HttpServletRequest request){
        try {
            return ResponseEntity.ok(new Response("success", transactionService.transferMoney(request.getHeader("token"))));
        }catch (BadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("error",e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("error",e.getMessage()));
        }
    }
}
