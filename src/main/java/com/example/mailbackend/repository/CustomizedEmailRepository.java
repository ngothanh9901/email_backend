package com.example.mailbackend.repository;


import com.example.mailbackend.dto.FindingEmailDTO;
import com.example.mailbackend.dto.FindingUserDTO;
import com.example.mailbackend.model.Email;
import com.example.mailbackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomizedEmailRepository {
    Page<Email> findEmail(FindingEmailDTO payload, Pageable pageable);
}
