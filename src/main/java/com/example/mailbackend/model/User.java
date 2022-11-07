package com.example.mailbackend.model;

import com.example.mailbackend.support.UserDateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String password;
    private String appPassword;

    @Column(name = "login_times")
    private Integer loginTimes = 0;

    private int loginFailedTimes = 0;

    private int emailNumber = 1;

    public User (String name,String username,String password,String appPassword){
        this.name = name;
        this.username = username;
        this.password = password;
        this.appPassword = appPassword;
    }
}
