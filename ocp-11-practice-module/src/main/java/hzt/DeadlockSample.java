package hzt;

public class DeadlockSample {

    //Q37 test 4
    //This is a complex question. Consider the following situation :
    //First thread acquires the lock of sb1. It appends X to sb1. Just after that, the OS stops this thread and starts the second thread. (Note that first thread still has the lock of sb1). Second thread acquires the lock of sb2 and appends Y to sb2. Now it tries to acquire the lock for sb1. But sb1's lock is already acquired by the first thread so the second thread has to wait. Now, the OS starts the first thread. It tries to acquire the lock of sb2 but cannot get it as the lock is already acquired by the second thread.
    //Here, you can see that both the threads are waiting for each other to release the lock. So in effect both are stuck! This is a classic case of a deadlock.
    //So the output cannot be determined.
    public static void main(String[] args) {
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        new Thread(() -> appendValsAndPrint(sb1, sb2, "X", "Y")).start();
        new Thread(() -> appendValsAndPrint(sb2, sb1, "Y", "X")).start();
    }

    private static void appendValsAndPrint(StringBuffer sb1, StringBuffer sb2, String sb1Val, String sb2Val) {
        synchronized (sb1) {
            sb1.append(sb1Val);
            synchronized (sb2) {
                sb2.append(sb2Val);
            }
        }
        System.out.println(sb1);
    }
}
