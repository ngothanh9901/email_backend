package com.example.mailbackend.service;

import javax.mail.internet.MimeMessage;

public interface ReceiveMailService {

    void handleReceivedMail(MimeMessage message);

}
