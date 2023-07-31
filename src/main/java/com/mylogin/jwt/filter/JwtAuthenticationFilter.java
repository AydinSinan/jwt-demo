package com.mylogin.jwt.filter;

import com.mylogin.jwt.service.CustomUserDetailService;
import com.mylogin.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


// Call this filter only once per request
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    CustomUserDetailService customUserDetailService;

    @Autowired
    JwtUtil jwtUtil;



    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // get the jwt token from request header
        // validate the jwt token
        String bearerToken = httpServletRequest.getHeader("Authorization");
        String username = null;
        String token = null;

        // check if token exist or has a Bearer text
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {

            // extract jwt token from bearerToken
            token = bearerToken.substring(7); // seventh is not include "Bearer "

            try{

                // extract username from the token
                username = jwtUtil.extractUsername(token);

                // get userdetails for this user
                UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

                // security checks
                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                    SecurityContextHolder.getContext().setAuthentication(upat);

                } else {
                    System.out.println("Invalid Token!!");
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            System.out.println("Invalid Bearer Token Format!!");
        }

        // if all is well forward the filter request to the request endpoint.
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
