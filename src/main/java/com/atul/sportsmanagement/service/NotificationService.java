package com.atul.sportsmanagement.service;

import com.atul.sportsmanagement.dto.NotificationResponse;
import com.atul.sportsmanagement.exceptions.SpringSportsException;
import com.atul.sportsmanagement.mapper.NotificationMapper;
import com.atul.sportsmanagement.model.Notification;
import com.atul.sportsmanagement.model.User;
import com.atul.sportsmanagement.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final AuthService authService;
    private final NotificationMapper notificationMapper;

    public void updateUserNotification(Notification notification, User user){
        notification.setUser(user);
        notification = save(notification);
        if(notification == null){
            throw new SpringSportsException("Notification not Updated");
        }
    }
    public Notification save(Notification notification){
        try{
            return notificationRepository.save(notification);
        }catch (Exception e) {
            return null;
        }
    }
    public Notification findByUser() {
        User user = authService.getCurrentUser();
        try {
            notificationRepository.findByUser(user);
        } catch (Exception e) {
            return null;
        }
        return null;
    }
    public List<NotificationResponse> findByUser(Integer limit){
        User user = authService.getCurrentUser();
        try{
            return notificationRepository.userNotification(user.getUserId(), PageRequest.of(0, limit)).stream()
                    .map(notificationMapper::mapToDto).collect(Collectors.toList());

        }catch (Exception e) {
            return new ArrayList<>();
        }
    }
    public Notification createNotificationObject(String message,Long eventId, String picture,String eventName){
        return new Notification(message,Instant.now(),eventId,picture,eventName);

    }
    public Notification findByUserAndNotificationId(User user,Long notificationId){
        try{
            return notificationRepository.findByUserAndNotificationId(user,notificationId);
        }catch (Exception e) {
            return null;
        }
    }
    public void saveNotificationToUsers(Set<User> users, String message,Long id,String picture,String eventName){
        users.forEach(System.out::println);
        try{
            users.forEach((user) -> updateUserNotification(createNotificationObject(message,id,picture,eventName),user));
        }
        catch (Exception ex){
            throw new SpringSportsException(ex.getMessage());
        }
    }


    public Integer getNotificationsCountByUser() {
        User user = authService.getCurrentUser();
        return notificationRepository.findByUser(user).size();
    }

    public boolean deleteNotification(Long id) {
        try{
            notificationRepository.deleteById(id);
            return true;
        }
        catch (Exception ex){
            return false;
        }

    }
}
