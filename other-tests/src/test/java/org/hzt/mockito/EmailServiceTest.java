package org.hzt.mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Argument that are passed to a method can also be captured by Mockito
 * <p>
 * <a href="https://www.baeldung.com/mockito-argumentcaptor">Using Mockito ArgumentCaptor</a>
 */
@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    final DeliveryPlatform deliveryPlatform;
    final EmailService emailService;

    @Captor
    ArgumentCaptor<Email> emailCaptor;

    EmailServiceTest(@Mock DeliveryPlatform deliveryPlatform) {
        this.deliveryPlatform = deliveryPlatform;
        this.emailService = new EmailService(deliveryPlatform);
    }

    @Test
    void whenEmailSent_captureArgumentsOfMailToBeDelivered() {
        String to = "info@baeldung.com";
        String subject = "Using ArgumentCaptor";
        String body = "Hey, let'use ArgumentCaptor";

        emailService.send(to, subject, body);

        verify(deliveryPlatform).deliver(emailCaptor.capture());

        Email capturedMail = emailCaptor.getValue();

        assertEquals("info@baeldung.com", capturedMail.to());
    }
}
