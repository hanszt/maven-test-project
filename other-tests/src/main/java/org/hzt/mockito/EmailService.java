package org.hzt.mockito;

/**
 * A class to test the argument capture feature from Mockito
 * <p>
 * <a href="https://www.baeldung.com/mockito-argumentcaptor">Using Mockito ArgumentCaptor</a>
 */
public class EmailService {

    private final DeliveryPlatform platform;

    public EmailService(DeliveryPlatform platform) {
        this.platform = platform;
    }

    public void send(String to, String subject, String body) {
        final var email = new Email(to, subject, body);
        platform.deliver(email);
    }
}
