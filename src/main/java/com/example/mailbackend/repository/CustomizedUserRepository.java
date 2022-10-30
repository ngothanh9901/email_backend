package com.example.mailbackend.repository;

import com.example.mailbackend.dto.FindingUserDTO;
import com.example.mailbackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomizedUserRepository {

    Page<User> findUser(FindingUserDTO payload, Pageable pageable);
}
