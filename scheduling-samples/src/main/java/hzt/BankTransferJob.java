package hzt;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankTransferJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankTransferJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        LOGGER.info("Job with fire instance id {} fired", jobExecutionContext.getFireInstanceId());
    }
}
