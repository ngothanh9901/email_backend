package com.example.mailbackend.controller;


import com.example.mailbackend.dto.EmailDTO;
import com.example.mailbackend.dto.EmailDetailDTO;
import com.example.mailbackend.dto.FindingEmailDTO;
import com.example.mailbackend.response.ResponseObject;
import com.example.mailbackend.service.EmailService;
import com.example.mailbackend.service.ReceiveMailServiceImpl;
import com.example.mailbackend.util.DataUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.mail.*;
import java.io.IOException;
import java.util.Properties;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    private static final Logger log = LoggerFactory.getLogger(ReceiveMailServiceImpl.class);
    @Autowired
    private EmailService emailService;

    @PostMapping("/")
    public ResponseObject<EmailDTO> findListEmail(@RequestBody FindingEmailDTO payload) throws MessagingException, IOException {
        Pageable pageable = DataUtils.getPageableForListLimit(payload.getPageNumber(),payload.getPageSize(),payload.getSortDir(),payload.getSortBy());
        return emailService.findEmail(payload,pageable);
    }

    @GetMapping("/{id}")
    public EmailDetailDTO findEmailById(@PathVariable("id") Long id){
        return emailService.findEmailById(id);
    }



    @GetMapping("/stmp")
    public void getEmailSTMP() throws MessagingException, IOException {
        emailService.getEmailFromSTMP();
    }

}
