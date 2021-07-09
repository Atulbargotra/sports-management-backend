package com.atul.sportsmanagement.repository;

import com.atul.sportsmanagement.model.Notification;
import com.atul.sportsmanagement.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Set<Notification> findByUser(User user);
    @Query("select n from Notification n where n.user.userId=:userId ORDER BY n.createdAt DESC")
    List<Notification> userNotification(@Param("userId") Long userId, Pageable pageSize);

    Notification findByUserAndNotificationId(User user,Long notificationId);
}
