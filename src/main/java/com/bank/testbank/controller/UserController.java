package com.bank.testbank.controller;

import com.bank.testbank.JwtUserDetailsService;
import com.bank.testbank.config.JwtTokenUtil;
import com.bank.testbank.dto.JwtRequest;
import com.bank.testbank.dto.Response;
import com.bank.testbank.dto.UserDto;
import com.bank.testbank.entity.User;
import com.bank.testbank.exception.BadRequestException;
import com.bank.testbank.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final Logger logger = LogManager.getLogger(getClass());

    @PostMapping("/register")
    public ResponseEntity<Response> createUser(@Valid @RequestBody UserDto user){
        try {
            return ResponseEntity.ok(new Response("success", userService.createUser(user)));
        }catch (BadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("error", e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("error", e.getMessage()));
        }
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginPage(){
        return ResponseEntity.ok("login page");
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        final String token = jwtTokenUtil.generateTokenForLogin(userDetails);
        User user = userService.getUserFromToken(token);
        ModelMap map = new ModelMap();
        map.addAttribute("token", token);
        return ResponseEntity.ok(map);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }catch (DisabledException e){
            throw new Exception("USER_DISABLED",e);
        }catch (BadCredentialsException e){
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
