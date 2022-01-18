package org.hzt.ssl_handshake_sample;

import javax.net.SocketFactory;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SSLClientSample {

    public static void main(String[] args) {
        String result = new SSLClientSample().makeConnection();
        System.out.println(result);
    }

    private String makeConnection() {
        String host = "localhost";
        int port = 8443;
        SocketFactory factory = SSLSocketFactory.getDefault();
        System.out.println("Setting up connection...");
        try (Socket connection = factory.createSocket(host, port)) {
            SSLSocket sslConnection = (SSLSocket) connection;
            sslConnection.setEnabledCipherSuites(new String[]{"TLS_DHE_DSS_WITH_AES_256_CBC_SHA256"});
            sslConnection.setEnabledProtocols(new String[]{"TLSv1.2"});

            SSLParameters sslParams = new SSLParameters();
            sslParams.setEndpointIdentificationAlgorithm("HTTPS");
            sslConnection.setSSLParameters(sslParams);
            System.out.printf("Reading inputstream from %s:%d...%n", host, port);
            BufferedReader input = new BufferedReader(new InputStreamReader(sslConnection.getInputStream()));
            return input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
