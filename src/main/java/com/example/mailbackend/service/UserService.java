package com.example.mailbackend.service;

import com.example.mailbackend.dto.FindingUserDTO;
import com.example.mailbackend.dto.UserDTO;
import com.example.mailbackend.dto.payload.UserPayload;
import com.example.mailbackend.model.User;
import com.example.mailbackend.repository.UserRepository;
import com.example.mailbackend.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void register(UserPayload userPayload){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userPayload.getPassword());
        System.out.println(encodedPassword);
        User user = new User(userPayload.getName(),userPayload.getUsername(), encodedPassword,userPayload.getAppPassword());

        userRepository.save(user);

    }

    public ResponseObject<UserDTO> findUser(FindingUserDTO payload, Pageable pageable) {
        Page<User> userPage = userRepository.findUser(payload, pageable);
        List<UserDTO> content = userPage.getContent().stream().map(u->mapToDTO(u)).collect(Collectors.toList());
        Page<UserDTO> data = new PageImpl<>(content, pageable, userPage.getTotalElements());

        ResponseObject<UserDTO> responseObject = new ResponseObject<>(data.getContent(),data.getNumber(),data.getSize(),
                data.getTotalElements(),data.getTotalPages(),data.isLast());

        return responseObject;
    }

    public UserDTO mapToDTO(User user){
        UserDTO userDTO = new UserDTO();

        userDTO.setName(user.getName());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setAppPassword(user.getAppPassword());

        return userDTO;
    }

}
