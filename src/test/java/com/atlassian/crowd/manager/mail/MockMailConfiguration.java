package com.atlassian.crowd.manager.mail;

import com.atlassian.crowd.util.mail.SMTPServer;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Collections;

public class MockMailConfiguration extends MailConfiguration {

    public static final String ADMIN  = "admin@localhost";
    public static final String FROM   = "crowd@localhost";
    public static final String PREFIX = "[Crowd]";
    public static final String HOST   = "smtp.example.com";

    public MockMailConfiguration() throws AddressException {
        super(MailConfiguration.builder()
                .setNotificationEmails(Collections.singletonList(ADMIN))
                .setSmtpServer(
                        SMTPServer.builder()
                                .setFrom(new InternetAddress(FROM))
                                .setPrefix(PREFIX)
                                .setHost(HOST)
                                .build()
                )
        );
    }

}
