package com.example.mailbackend.service;

import com.example.mailbackend.dto.EmailDTO;
import com.example.mailbackend.dto.EmailDetailDTO;
import com.example.mailbackend.dto.FindingEmailDTO;
import com.example.mailbackend.model.Email;
import com.example.mailbackend.model.User;
import com.example.mailbackend.repository.EmailRepository;
import com.example.mailbackend.repository.UserRepository;
import com.example.mailbackend.response.ResponseObject;
import com.example.mailbackend.security.SecurityUtils;
import com.example.mailbackend.security.UserPrincipal;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class EmailService {
    @Autowired
    private UserService userService;
    private static final Logger log = LoggerFactory.getLogger(ReceiveMailServiceImpl.class);
    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private UserRepository userRepository;
    public ResponseObject<EmailDTO> findEmail(FindingEmailDTO payload, Pageable pageable) throws MessagingException, IOException {

        UserPrincipal userPrincipal = SecurityUtils.getUserPrincipal();
        long id = userPrincipal.getId();
        User user =userService.findById(id);

        Long numberEmail = emailRepository.countByUser(user);
//        Long numberEmail = Long.valueOf(0);
        ResponseObject<EmailDTO> responseObject = new ResponseObject<EmailDTO>();

        if((payload.getPageNumber()+1)*payload.getPageSize()>= numberEmail){
            List<Email> listEmails = getEmailFromSTMP();
            if(listEmails!=null){
                for(Email email : listEmails){
                    emailRepository.save(email);
                }
                user.setEmailNumber(user.getEmailNumber()+listEmails.size());
                userRepository.save(user);
            }
        }
        Page<Email> userPage = emailRepository.findEmail(payload, pageable);
        List<EmailDTO> content = userPage.getContent().stream().map(u->mapToDTO(u)).collect(Collectors.toList());
        Page<EmailDTO> data = new PageImpl<>(content, pageable, userPage.getTotalElements());

        responseObject = new ResponseObject<>(data.getContent(),data.getNumber(),data.getSize(),
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

    public List<Email> getEmailFromSTMP() throws MessagingException, IOException {
        UserPrincipal userPrincipal = SecurityUtils.getUserPrincipal();
        long id = userPrincipal.getId();
        User user =userService.findById(id);

        List<Email> listEmail = new ArrayList<>();
        Folder folder = null;
        Store store = null;

        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props, null);

        store = session.getStore("imaps");
        store.connect("imap.gmail.com", "gdgaerd@gmail.com", "bbovwgrfoqprjfsn");
 //       store.connect("imap.gmail.com",user.getUsername(),user.getAppPassword());

        folder = store.getFolder("Inbox");
        folder.open(Folder.READ_WRITE);

        try {
            Message messages[] = folder.getMessages(user.getEmailNumber()+1,2);

            for (int i = 0; i < messages.length; ++i) {
                log.info("Email Number " + (i + 1));
                Message message = messages[i];
                if (message.isMimeType("text/plain") || message.isMimeType("text/html")) {
                    log.info("Email Number " + (i + 1));
                    log.info("From: " + message.getFrom()[0]);
                    log.info("Subject: " + message.getSubject());
                    log.info("Body: " + message.getContent().toString());
                    log.info("--------------------------------");
                } else {
                    Multipart multipart = (Multipart) messages[i].getContent();

//                for (int j = 0; j < multipart.getCount(); j++) {

                    BodyPart bodyPart = multipart.getBodyPart(0);

                    String disposition = bodyPart.getDisposition();

                    if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) { // BodyPart.ATTACHMENT doesn't work for gmail
                        System.out.println("Mail have some attachment");
                        DataHandler handler = bodyPart.getDataHandler();
                        System.out.println("file name : " + handler.getName());
                    } else {
//                        log.info("Email Number " + (i + 1));
                        log.info("From: " + message.getFrom()[0].toString());
                        log.info("Subject: " + message.getSubject());
                        log.info("Body: " + bodyPart.getContent());
//                        String content= bodyPart.getContent().toString();
                       Email email = new Email(message.getFrom()[0].toString(),message.getSubject(),bodyPart.getContent().toString(),user);
//                        Email email = new Email(message.getFrom()[0].toString(), message.getSubject(), bodyPart.getContent().toString(), null);
                        listEmail.add(email);
                    }
                    //               }
                    log.info("--------------------------------");
//            else {
//                log.info("Email Number " + (i + 1));
                }

            }
        }catch (Exception ex){
            System.out.println(ex);
            return null;
        }



        return listEmail;
    }

}
