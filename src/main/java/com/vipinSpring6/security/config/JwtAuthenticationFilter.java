package com.vipinSpring6.security.config;

import com.vipinSpring6.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");// auth header remember from postman

        if(authHeader == null || !authHeader.startsWith("Bearer")){// check that authHeader is null or not if null
            filterChain.doFilter(request, response); // do next filter from filterChain
            return;
        }
        // now authHeader is not null
        final String jwt = authHeader.substring(7);// remove first 7 word of bearer
        final String userName = jwtService.extractUserName(jwt);// according to token extracting the userName from token

        Authentication authentication // getting that i am authenticate or not
                = SecurityContextHolder.getContext().getAuthentication();

        if(userName != null && authentication == null){ // username is present and not authenticate then authenticate this useName
            // do Authentication operation
            UserDetails userDetails// getting data of this username from db
                    = userDetailsService.loadUserByUsername(userName);

            // check if jwt is valid or not
            if(jwtService.isTokenValid(jwt, userDetails)){// checking that db data's and token data is matching or not
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken( // making the password authenticate
                                userDetails, null,userDetails.getAuthorities()
                );// pass this information in the next filter
                usernamePasswordAuthenticationToken.setDetails(// setting the session id and url
                        new WebAuthenticationDetailsSource()    // it will set the seesion id and remote address
                                .buildDetails(request)
                );
                SecurityContextHolder.getContext()// after verify and all add tis token in security context
                        .setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);// after this it will call the next filter
    }
}
