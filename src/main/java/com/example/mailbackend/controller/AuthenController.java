package com.example.mailbackend.controller;

import com.example.mailbackend.dto.AuthRequest;
import com.example.mailbackend.dto.AuthResponse;
import com.example.mailbackend.security.UserPrincipal;
import com.example.mailbackend.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


@Controller
@RequestMapping("/api/auth")
public class AuthenController {
    ////JWT
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtTokenUtil jwtUtil;




    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword())
            );

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();


            String accessToken = jwtUtil.generateAccessToken(userPrincipal);

            System.out.println(userPrincipal.getId());
//            Token token = tokenService.createRefreshToken(userPrincipal.getId(), accessToken);
//            AuthResponse response = new AuthResponse(userPrincipal.getUsername(), accessToken, token.getRefreshToken());

            AuthResponse response = new AuthResponse(userPrincipal.getUsername(), accessToken);
            return ResponseEntity.ok().body(response);
        } catch(AuthenticationException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

//    @PostMapping("/refreshToken")
//    public ResponseEntity<?> refreshtoken(@Valid @RequestBody RefreshTokenRequest request) {
//        String requestRefreshToken = request.getRefreshToken();
//
//        return tokenService.findByRefreshToken(requestRefreshToken)
//                .map(tokenService::verifyExpiration)
//                .map(Token::getUser)
//                .map(user -> {
//                    String accessToken = jwtUtil.generateAccessToken(UserPrincipal.create(user));
//                    tokenService.saveAccessToken(user.getId(), accessToken,requestRefreshToken);
//                    return ResponseEntity.ok(new TokenRefreshResponse(accessToken, requestRefreshToken));
//                })
//                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(@Valid @RequestBody RefreshTokenRequest request) {
//
//        try{
////            UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////
////            System.out.println(userPrincipal.getId());
//            tokenService.deleteByRefershToken(request.getRefreshToken());
//            return ResponseEntity.ok().body("Logout successfully");
//        }catch(Exception ex){
//            return ResponseEntity.badRequest().body("Token Invalid");
//        }
//    }

//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
//        System.out.println(encodedPassword);
//        User user = new User(userDTO.getUsername(), encodedPassword);
//
//        userRepo.save(user);
//
//        return ResponseEntity.ok().body("register successfully");
//    }

}
