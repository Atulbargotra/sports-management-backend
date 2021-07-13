package com.atul.sportsmanagement.mapper;

import com.atul.sportsmanagement.dto.FeedbackRequest;
import com.atul.sportsmanagement.dto.FeedbackResponse;
import com.atul.sportsmanagement.model.Feedback;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component

public class FeedbackMapper {
    public Feedback map(FeedbackRequest feedbackRequest, Long userId, Long eventId){
        Feedback feedback = new Feedback();
        feedback.setFeedback(feedbackRequest.getMessage());
        feedback.setRating(feedbackRequest.getRating());
        feedback.setEventId(eventId);
        feedback.setUserId(userId);
        return feedback;
    }
    public FeedbackResponse mapToDto(Set<Feedback> feedbacks){
        FeedbackResponse response = new FeedbackResponse();
        response.setRatings(feedbacks.stream().collect(Collectors.groupingBy(Feedback::getRating,Collectors.counting())));
        response.setFeedback(feedbacks.stream().map(Feedback::getFeedback).collect(Collectors.toList()));
        return response;
    }
}
