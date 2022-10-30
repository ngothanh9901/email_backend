package com.example.mailbackend.dto;

import com.example.mailbackend.model.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetailDTO {
    private String fromEmail;
    private String subject;
    private String body;

    public static EmailDetailDTO mapToDTO(Email email){
        EmailDetailDTO emailDetailDTO = new EmailDetailDTO();

        emailDetailDTO.setFromEmail(email.getFromEmail());
        emailDetailDTO.setSubject(email.getSubject());
        emailDetailDTO.setBody(email.getBody());

        return emailDetailDTO;
    }
}
