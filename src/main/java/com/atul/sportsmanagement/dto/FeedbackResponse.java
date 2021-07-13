package com.atul.sportsmanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FeedbackResponse {
    private Map<Integer,Long> ratings;
    private List<String> feedback;
}
