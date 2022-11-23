package com.example.mailbackend.repository;

import com.example.mailbackend.model.Email;
import com.example.mailbackend.model.Lable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LableRespository extends JpaRepository<Lable, Long> {
    Lable findByName(String name);
}
