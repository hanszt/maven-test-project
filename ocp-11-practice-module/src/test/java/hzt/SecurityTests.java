package hzt;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SecurityTests {

    @Test
    void testDoPrivilegedGuidelines() {
        System.getProperties().entrySet().forEach(System.out::println);
        assertNotEquals("not a valid property", getJavaVersion("java.specification.version"));
    }

    //q51 test 4
//    As per Guideline 9-3 / ACCESS-3: "Safely invoke java.security.AccessController.doPrivileged",
//    the given code should retrieve a system property using a hardcoded value instead of passing user input directly to the OS.
//    In the given code, the user can potentially wreck the application by requesting ill formatted or mischievous property name.
//    Since the code is privileged, the call may cause unwanted impact directly on the OS.

    //    Ideally, it should validate whether the property name for which the value is requested is valid or not.
    public static String getJavaVersion(String property) {
        if (System.getProperties().containsKey(property)) {
            return AccessController.doPrivileged((PrivilegedAction<String>) () -> System.getProperty(property));
        }
        return "not a valid property";
    }

    @Test
    void testPrivilegedSocketConnection() {
        assertThrows(PrivilegedActionException.class, () -> invokeService("test", "hans", 2));
    }

    //q52 test 4
    //You also notice the following permission that is given to this code in the security policy file:
    //
    // permission java.io.SocketPermission "*", "connect";
    //
    // What security vulnerability does this expose to your cloud customer's code?
    //
    //Answer: Denial of service attack against any reachable host.
    //
    //Explanation: Letting socket connections to be opened to any host has the potential
    // to cause a denial of service attack against that host.
    public void invokeService(String servername, String host, int port) throws IOException, PrivilegedActionException {
        Socket socket = AccessController.doPrivileged((PrivilegedExceptionAction<Socket>) () -> new Socket(host, port));
        socket.connect(new InetSocketAddress(servername, port));
    }
}
