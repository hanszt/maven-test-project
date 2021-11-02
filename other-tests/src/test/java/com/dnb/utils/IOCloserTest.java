package com.dnb.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IOCloserTest {

    @Test
    void testAutoCloser() {
        ResourceNotImplementingClosable resource1 = new ResourceNotImplementingClosable("Resource 1");
        ResourceNotImplementingClosable resource2 = new ResourceNotImplementingClosable("Resource 2");
        ResourceNotImplementingClosable resource3 = new ResourceNotImplementingClosable("Resource 3");
        try (var ioCloser = new IOCloser()) {
            ioCloser.addCloseFunctions(resource1::close, resource2::close, resource3::close);
            assertFalse(resource1.closed);
            assertFalse(resource2.closed);
            assertFalse(resource3.closed);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(resource1.closed);
        assertTrue(resource2.closed);
        assertTrue(resource3.closed);
    }

    @Test
    void testOneOfCloseMethodsThrowingException() {
        ResourceNotImplementingClosable resource1 = new ResourceNotImplementingClosable("Resource 1");
        ResourceNotImplementingClosable resource2 = new ResourceNotImplementingClosable("Resource 2");
        ResourceNotImplementingClosable resource3 = new ResourceNotImplementingClosable("Resource 3");
        try (var ioCloser = new IOCloser()) {
            resource1.load();
            ioCloser.addCloseFunctions(resource1::close, resource2::closeThrowingException, resource3::close);

        } catch (IOException e) {
            assertEquals("Could not close all", e.getMessage());
            e.printStackTrace();
        }
        assertTrue(resource1.closed);
        assertFalse(resource2.closed);
        assertTrue(resource3.closed);
    }

    private static class ResourceNotImplementingClosable {

        private final String name;
        private boolean closed;

        public ResourceNotImplementingClosable(String name) {
            this.name = name;
        }


        public void load() throws IOException {
            System.out.println(name + " loading...");
            System.out.println(name + " loaded");
        }

        public void close() {
            System.out.println(name + " is now closed");
            closed = true;
        }

        public void closeThrowingException() throws IOException {
            throw new IOException("Could not close " + name);
        }
    }
}
