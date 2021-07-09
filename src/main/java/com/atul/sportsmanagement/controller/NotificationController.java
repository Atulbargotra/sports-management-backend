package com.atul.sportsmanagement.controller;

import com.atul.sportsmanagement.dto.NotificationResponse;
import com.atul.sportsmanagement.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/notifications/user")
public class NotificationController {
    private NotificationService notificationService;

    @GetMapping
    public List<NotificationResponse> getNotificationsByUser(@RequestParam(name = "limit",required = false,defaultValue = "10") String limit) {
        return notificationService.findByUser(Integer.parseInt(limit));
    }
    @GetMapping("/count")
    public Integer getNotificationsCountByUser(){
        return notificationService.getNotificationsCountByUser();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteNotification(@PathVariable Long id){
        return new ResponseEntity<>(notificationService.deleteNotification(id), HttpStatus.OK);
    }
}
