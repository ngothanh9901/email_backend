package com.example.mailbackend.repository;

import com.example.mailbackend.model.Email;
import com.example.mailbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long>,CustomizedEmailRepository{
    public Long countByUser(User user);
}
