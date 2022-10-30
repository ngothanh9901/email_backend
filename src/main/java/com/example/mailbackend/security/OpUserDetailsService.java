package com.example.mailbackend.security;

import com.example.mailbackend.exception.ResourceNotFoundException;
import com.example.mailbackend.model.User;
import com.example.mailbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OpUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Let people login with either username or email
        Optional<User> user = userRepository.findByUsernameIgnoreCaseAndDeletedFalse(username);
        user.orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email : " + username)
        );
        return UserPrincipal.create(user.get());
    }


    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }
}
