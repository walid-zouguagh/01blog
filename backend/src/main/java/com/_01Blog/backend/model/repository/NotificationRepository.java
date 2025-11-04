package com._01Blog.backend.model.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._01Blog.backend.model.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID>{

}
