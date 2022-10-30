package com.example.mailbackend.controller;

import com.example.mailbackend.dto.FindingUserDTO;
import com.example.mailbackend.dto.UserDTO;
import com.example.mailbackend.dto.payload.UserPayload;
import com.example.mailbackend.model.User;
import com.example.mailbackend.repository.UserRepository;
import com.example.mailbackend.response.ResponseObject;
import com.example.mailbackend.service.UserService;
import com.example.mailbackend.util.DataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;


    @RequestMapping(path = "/register", method = POST)
    public ResponseEntity<?> register (@RequestBody UserPayload userPayload) {

        userService.register(userPayload);
        return ResponseEntity.ok().body("register successfully");
    }



    @PostMapping("/")
    public ResponseObject<UserDTO> findListJob(@RequestBody FindingUserDTO findingUserDTO) {
        Pageable pageable = DataUtils.getPageableForListLimit(findingUserDTO.getPageNumber(),findingUserDTO.getPageSize(),findingUserDTO.getSortDir(),findingUserDTO.getSortBy());
        return userService.findUser(findingUserDTO,pageable);
    }
    public User update(){
        return null;
    }
}

