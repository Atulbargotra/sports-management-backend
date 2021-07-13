package com.atul.sportsmanagement.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class FeedbackRequest {

    private Integer rating;

    @NotBlank(message = "message cannot be blank")
    private String message;

}
