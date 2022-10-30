package com.example.mailbackend.service;

import com.example.mailbackend.dto.EmailDTO;
import com.example.mailbackend.dto.FindingEmailDTO;
import com.example.mailbackend.model.Email;
import com.example.mailbackend.repository.EmailRepository;
import com.example.mailbackend.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;
    public ResponseObject<EmailDTO> findEmail(FindingEmailDTO payload, Pageable pageable) {
        Page<Email> userPage = emailRepository.findEmail(payload, pageable);
        List<EmailDTO> content = userPage.getContent().stream().map(u->mapToDTO(u)).collect(Collectors.toList());
        Page<EmailDTO> data = new PageImpl<>(content, pageable, userPage.getTotalElements());

        ResponseObject<EmailDTO> responseObject = new ResponseObject<>(data.getContent(),data.getNumber(),data.getSize(),
                data.getTotalElements(),data.getTotalPages(),data.isLast());

        return responseObject;
    }

    public EmailDTO mapToDTO(Email email){
        EmailDTO emailDTO = new EmailDTO();

        emailDTO.setId(emailDTO.getId());
        emailDTO.setFromEmail(email.getFromEmail());
        emailDTO.setSubject(emailDTO.getSubject());

        return emailDTO;
    }
}
