package com.example.mailbackend.controller;


import com.example.mailbackend.dto.EmailDTO;
import com.example.mailbackend.dto.FindingEmailDTO;
import com.example.mailbackend.response.ResponseObject;
import com.example.mailbackend.service.EmailService;
import com.example.mailbackend.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/")
    public ResponseObject<EmailDTO> findListEmail(@RequestBody FindingEmailDTO payload) {
        Pageable pageable = DataUtils.getPageableForListLimit(payload.getPageNumber(),payload.getPageSize(),payload.getSortDir(),payload.getSortBy());
        return emailService.findEmail(payload,pageable);
    }
}
