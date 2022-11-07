package com.example.mailbackend.controller;


import javax.mail.*;
import java.io.IOException;
import java.util.Properties;

import com.example.mailbackend.service.ReceiveMailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestEmail {
    private static final Logger log = LoggerFactory.getLogger(ReceiveMailServiceImpl.class);

    public static void doit() throws MessagingException, IOException {

        Folder folder = null;
        Store store = null;

        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props, null);

        store = session.getStore("imaps");
        store.connect("imap.gmail.com", "ngothanh9901@gmail.com", "jtaiyvxkqwnwhlxl");

        folder = store.getFolder("Inbox");
        folder.open(Folder.READ_WRITE);

        Message messages[] = folder.getMessages();

        for (int i = 0; i < messages.length; ++i) {
            Message message = messages[i];
            if (message.isMimeType("text/plain")) {
                log.info("--------------------------------");
                log.info("Email Number " + (i + 1));
                log.info("From: " + message.getFrom()[0]);
                log.info("Subject: " + message.getSubject());
                log.info("Body: "+message.getContent().toString());
            }else {
                log.info("Email Number " + (i + 1));
            }
        }
    }

}
