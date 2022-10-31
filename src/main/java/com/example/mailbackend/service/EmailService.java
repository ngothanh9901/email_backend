package com.example.mailbackend.service;

import com.example.mailbackend.dto.EmailDTO;
import com.example.mailbackend.dto.EmailDetailDTO;
import com.example.mailbackend.dto.FindingEmailDTO;
import com.example.mailbackend.model.Email;
import com.example.mailbackend.repository.EmailRepository;
import com.example.mailbackend.response.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.*;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(ReceiveMailServiceImpl.class);
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

        emailDTO.setId(email.getId());
        emailDTO.setFromEmail(email.getFromEmail());
        emailDTO.setSubject(email.getSubject());

        return emailDTO;
    }

    public EmailDetailDTO findEmailById(Long id){
        Email email = emailRepository.findById(id).get();
        EmailDetailDTO emailDetailDTO = EmailDetailDTO.mapToDTO(email);
        return emailDetailDTO;
    }

    public void getEmailFromSTMP() throws MessagingException, IOException {

        Folder folder = null;
        Store store = null;

        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props, null);

        store = session.getStore("imaps");
        store.connect("imap.gmail.com", "gdgaerd@gmail.com", "bbovwgrfoqprjfsn");

        folder = store.getFolder("Inbox");
        folder.open(Folder.READ_WRITE);

        Message messages[] = folder.getMessages();

        for (int i = 0; i < messages.length; ++i) {
//            log.info("Email Number " + (i + 1));
            Message message = messages[i];
            if (message.isMimeType("text/plain") || message.isMimeType("text/html")) {
                log.info("Email Number " + (i + 1));
                log.info("From: " + message.getFrom()[0]);
                log.info("Subject: " + message.getSubject());
                log.info("Body: "+message.getContent().toString());
                log.info("--------------------------------");
            }
            else {
                Multipart multipart = (Multipart) messages[i].getContent();

                for (int j = 0; j < multipart.getCount(); j++) {

                    BodyPart bodyPart = multipart.getBodyPart(j);

                    String disposition = bodyPart.getDisposition();

                    if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) { // BodyPart.ATTACHMENT doesn't work for gmail
                        System.out.println("Mail have some attachment");
                        DataHandler handler = bodyPart.getDataHandler();
                        System.out.println("file name : " + handler.getName());
                    }
                    else {
//                        log.info("Email Number " + (i + 1));
                        log.info("From: " + message.getFrom()[0]);
                        log.info("Subject: " + message.getSubject());
                        log.info("Body: "+bodyPart.getContent().toString());
//                        String content= bodyPart.getContent().toString();
                    }
                }
                log.info("--------------------------------");
//            else {
//                log.info("Email Number " + (i + 1));
            }
        }
    }

}
