package com.atul.sportsmanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.quartz.JobKey;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailResponse {
    private boolean success;

    private String jobId;

    private String jobGroup;

    private String message;

    private JobKey jobKey;

    public EmailResponse(boolean success, JobKey jobKey, String jobId, String jobGroup, String message) {
        this.success = success;
        this.jobKey = jobKey;
        this.jobId = jobId;
        this.jobGroup = jobGroup;
        this.message = message;
    }

    public EmailResponse(boolean success, String message){
        this.success = success;
        this.message = message;
    }
}

