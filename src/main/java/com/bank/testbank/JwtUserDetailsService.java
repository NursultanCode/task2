package com.bank.testbank;

import com.bank.testbank.entity.User;
import com.bank.testbank.repository.UserRepository;
import com.bank.testbank.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Service("userDetailService")
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginAttemptService loginAttemptService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)){
            throw new RuntimeException("blocked");
        }
        try {
            User user = userRepository.findByContactNo(username);
            if (user==null){
                throw new UsernameNotFoundException("User not found with number: "+username);
            }
            return new org.springframework.security.core.userdetails.User(user.getContactNo(), user.getPin(), new ArrayList<>());
        }catch (Exception e){
            throw new UsernameNotFoundException("User not found with number: "+username);
        }
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
