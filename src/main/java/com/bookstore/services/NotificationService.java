package com.bookstore.services;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    public void notifyAuthor(String authorEmail, String bookTitle) {
        NotificationThread notificationThread = new NotificationThread(authorEmail, bookTitle);
        notificationThread.start();
    }
} 