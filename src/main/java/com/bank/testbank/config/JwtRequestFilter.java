package com.bank.testbank.config;

import com.bank.testbank.JwtUserDetailsService;
import com.bank.testbank.exception.MyJwtTokenExpiredException;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    private final Logger logger = LogManager.getLogger(getClass());
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("token");
        String username = null;
        String jwtToken = null;
        logger.info(request.getRequestURL());
        if (requestTokenHeader!=null) {
            jwtToken = requestTokenHeader;
            try {
                username = jwtTokenUtil.getUserNameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.info("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.info("JWT Token has expired");
                throw new MyJwtTokenExpiredException("Token Expired");
            }
        }else {
            logger.warn("JWT token does not found");
        }
        if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
