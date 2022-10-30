package com.example.mailbackend.util;


import com.example.mailbackend.repository.UserRepository;
import com.example.mailbackend.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailServices;

//    @Autowired
//    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getAccessToken(request);

        try{
            if (!jwtUtil.validateAccessToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }
        } catch (Exception ex){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Token Expired");
                return;
        }



        if (!checkAccessToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        setAuthenticationContext(token, request);
        filterChain.doFilter(request, response);

    }

    private boolean checkAccessToken(String accessToken) {
        String[] jwtSubject = jwtUtil.getSubject(accessToken).split(",");

        Long id = Long.parseLong(jwtSubject[0]);

    //    String username = jwtSubject[1];
//
//        List<Token> listToken = tokenService.findByUserId(id);
//
//        for(Token token:listToken){
//            if(token.getAccessToken().equals(accessToken)==true) return true;
//        }

        return false;
    }

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return !ObjectUtils.isEmpty(header);
    }

    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
//        String token = header.split(" ")[1].trim();
        String token = header.trim();
        return token;
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(token);


        List<GrantedAuthority> authorities = (List<GrantedAuthority>) userDetails.getAuthorities();
//	        authorities.add(new SimpleGrantedAuthority("ROLE_"+"ADMIN"));

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserDetails getUserDetails(String token) {

        String[] jwtSubject = jwtUtil.getSubject(token).split(",");

        Long id = Long.parseLong(jwtSubject[0]);
        String username = jwtSubject[1];

//	        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//	        authorities.add(new SimpleGrantedAuthority(""));

        UserPrincipal userPrincipal = (UserPrincipal) userDetailServices.loadUserByUsername(username);
        return userPrincipal;

    }

}
