package hzt;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <a href="https://www.youtube.com/watch?v=uDkleDfmvBg">
 * Getting Started: Scheduling Tasks with Quartz | Scheduling Tasks in Java - Quartz & Cron</a>
 */
public final class QuartzSample {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzSample.class);

    private final Clock clock;

    public QuartzSample(Clock clock) {
        this.clock = clock;
    }

    public Scheduler start() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            JobDetail jobDetail = JobBuilder.newJob(BankTransferJob.class)
                    .withIdentity("bank-transfer")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger-1")
                    .startAt(Date.from(LocalDateTime.now(clock)
                            .plusSeconds(4)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()))
                    .withSchedule(SimpleScheduleBuilder
                            .simpleSchedule()
                            .withIntervalInMilliseconds(500)
                            .withRepeatCount(10))
                    .build();

            final var date = scheduler.scheduleJob(jobDetail, trigger);

            final var localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            LOGGER.info("localDate = {}", localDate);

            scheduler.start();
            return scheduler;
        } catch (SchedulerException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String... args) {
        LOGGER.info("Quartz sample started");

        final var scheduler = new QuartzSample(Clock.systemDefaultZone()).start();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            @SuppressWarnings("all")
            public void run() {
                try {
                    scheduler.shutdown();
                } catch (SchedulerException e) {
                    LOGGER.error("Scheduler exception occurred", e);
                }
                System.exit(0);
            }
        }, Duration.ofSeconds(10).toMillis());
    }
}
