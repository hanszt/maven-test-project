package org.hzt.ssl_handshake_sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.SocketFactory;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static org.hzt.utils.It.printf;
import static org.hzt.utils.It.println;

public class SSLClientSample {

    private static final Logger LOGGER = LoggerFactory.getLogger(SSLClientSample.class);

    public static void main(String... args) {
        String result = SSLClientSample.makeConnection();
        LOGGER.info(result);
    }

    private static String makeConnection() {
        String host = "localhost";
        int port = 8443;
        SocketFactory factory = SSLSocketFactory.getDefault();
        println("Setting up connection...");
        try (Socket connection = factory.createSocket(host, port)) {
            SSLSocket sslConnection = (SSLSocket) connection;
            sslConnection.setEnabledCipherSuites(new String[]{"TLS_DHE_DSS_WITH_AES_256_CBC_SHA256"});
            sslConnection.setEnabledProtocols(new String[]{"TLSv1.2"});

            SSLParameters sslParams = new SSLParameters();
            sslParams.setEndpointIdentificationAlgorithm("HTTPS");
            sslConnection.setSSLParameters(sslParams);
            printf("Reading inputstream from %s:%d...%n", host, port);
            BufferedReader input = new BufferedReader(new InputStreamReader(sslConnection.getInputStream()));
            return input.readLine();
        } catch (IOException e) {
            LOGGER.error("Could not establish connection", e);
            return "";
        }
    }
}
