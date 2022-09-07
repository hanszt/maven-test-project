package hzt;

import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;

import java.time.Clock;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.jupiter.api.Assertions.*;

class QuartzSampleTest {

    @Test
    void testCreateAndStartScheduler() throws SchedulerException {
        final var scheduler = new QuartzSample(Clock.systemDefaultZone()).start();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    scheduler.shutdown();
                } catch (SchedulerException e) {
                    throw new IllegalStateException(e);
                }
                System.exit(0);
            }
        }, Duration.ofSeconds(10).toMillis());
        assertTrue(scheduler.isStarted());
    }

}
