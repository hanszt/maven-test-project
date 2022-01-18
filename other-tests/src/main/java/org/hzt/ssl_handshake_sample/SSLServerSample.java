package org.hzt.ssl_handshake_sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class SSLServerSample {

    private static final Logger LOGGER = LoggerFactory.getLogger(SSLServerSample.class);
    
    public static void main(String[] args) {
        var startTime = LocalDateTime.now();
        var durationTillShutdown = Duration.ofMinutes(15);
        setupShutdownTimer(durationTillShutdown);
        LOGGER.info("Server starting...");
        var port = 8443;
        var factory = SSLServerSocketFactory.getDefault();
        try (var listener = factory.createServerSocket(port)) {
            var sslListener = (SSLServerSocket) listener;
            sslListener.setNeedClientAuth(true);
            sslListener.setEnabledCipherSuites(new String[]{"TLS_DHE_DSS_WITH_AES_256_CBC_SHA256"});
            sslListener.setEnabledProtocols(new String[]{"TLSv1.2"});
            LOGGER.info("Server started. Listening on port {}", port);
            while (LocalDateTime.now().minus(durationTillShutdown).isBefore(startTime)) {
                try (var socket = sslListener.accept()) {
                    var out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("Hello World!");
                }
                LOGGER.info("Request made");
            }
        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }

    private static void setupShutdownTimer(Duration duration) {
        var timerTask = new TimerTask() {
            @Override
            public void run() {
                shutDownServer(duration);
            }
        };
        var timer = new Timer();
        timer.schedule(timerTask, duration.toMillis());
    }

    private static void shutDownServer(Duration duration) {
        LOGGER.info("Timed server shutdown. Run duration: {} seconds", duration.toSeconds());
    }
}
