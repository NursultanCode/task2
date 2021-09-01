package com.bank.testbank.service;

import com.bank.testbank.config.JwtTokenUtil;
import com.bank.testbank.dto.UserDto;
import com.bank.testbank.entity.Account;
import com.bank.testbank.entity.User;
import com.bank.testbank.exception.BadRequestException;
import com.bank.testbank.repository.AccountRepository;
import com.bank.testbank.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final Logger logger = LogManager.getLogger(getClass());

    public String createUser(UserDto userDto){
        if (userRepository.findByContactNo(userDto.getContactNo())!=null){
            logger.info("user already exist with mobile number:{}", userDto.getContactNo());
            throw new BadRequestException("User Already registered");
        }
        User user = new User();
        user.setPin(userDto.getPin());
        user.setContactNo(userDto.getContactNo());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPin(encoder.encode(user.getPin()));
        Account account = new Account();
        account.setBalance(8.0);
        user.setAccount(account);
        account.setUser(user);
        userRepository.save(user);
        return "Account created";
    }

    public User getUserFromToken(String token){
        String contactNo = jwtTokenUtil.getUserNameFromToken(token);
        return userRepository.findByContactNo(contactNo);
    }

}
