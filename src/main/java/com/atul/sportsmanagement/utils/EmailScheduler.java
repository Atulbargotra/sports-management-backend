package com.atul.sportsmanagement.utils;

import com.atul.sportsmanagement.dto.EmailRequest;
import com.atul.sportsmanagement.dto.EmailResponse;
import com.atul.sportsmanagement.quartz.job.EmailJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Component
public class EmailScheduler {
    private final Scheduler scheduler;

    public EmailScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    private JobDetail buildJobDetail(EmailRequest scheduleEmailRequest) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("email", scheduleEmailRequest.getEmail());
        jobDataMap.put("subject", scheduleEmailRequest.getSubject());
        jobDataMap.put("body", scheduleEmailRequest.getBody());

        return JobBuilder.newJob(EmailJob.class)
                .withIdentity(UUID.randomUUID().toString(), "email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-triggers")
                .withDescription("Send Email Trigger")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }

    public EmailResponse scheduleEmail(EmailRequest emailRequest) {
        try {
            ZonedDateTime dateTime = ZonedDateTime.of(emailRequest.getDateTime(), emailRequest.getTimeZone());
            System.out.println(dateTime);
            if (dateTime.isBefore(ZonedDateTime.now())) {
                return new EmailResponse(false, "dateTime must be after current time");
            }
            JobDetail jobDetail = buildJobDetail(emailRequest);
            Trigger trigger = buildTrigger(jobDetail, dateTime);

            scheduler.scheduleJob(jobDetail,trigger);

            return new EmailResponse(true, jobDetail.getKey(),jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), "Email Scheduled Successfully!");
        } catch (SchedulerException se) {
            log.error("Error while scheduling email: ", se);
            return new EmailResponse(false,
                    "Error while scheduling email");
        }
    }
    public void deleteJob(JobKey jobKey){
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            log.error("Exception while deleting job", e);
        }

    }
}
