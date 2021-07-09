package com.atul.sportsmanagement.mapper;

import com.atul.sportsmanagement.dto.NotificationResponse;
import com.atul.sportsmanagement.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationResponse mapToDto(Notification notification){
        NotificationResponse notificationResponse = new NotificationResponse();
        notificationResponse.setId(notification.getNotificationId());
        notificationResponse.setMessage(notification.getMessage());
        notificationResponse.setDate(notification.getCreatedAt());
        notificationResponse.setEventId(notification.getEventId());
        notificationResponse.setPicture(notification.getPicture());
        return notificationResponse;
    }
}
