package hzt.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LockTests {

    static class Account {

        private String id;
        private double balance;
        private static final ReentrantLock lock = new ReentrantLock();

        public void withdraw(double amt) {
            try {
                lock.lock();
                if (balance > amt) {
                    balance = balance - amt;
                }
            } finally {
                lock.unlock();
            }
        }

        public String getId() {
            return id;
        }

        public double getBalance() {
            return balance;
        }
    }

    @Test
    void testLockWhenPrivateStaticFinalMakesMethodThreadSafe() {
        final var account = new Account();
        account.withdraw(34);
        assertEquals(0.0, account.getBalance());
    }
}
