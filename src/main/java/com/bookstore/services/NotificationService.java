package com.bookstore.services;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    /**
     * Sends notification to the author asynchronously using a separate thread.
     * 
     * @param authorEmail Email address of the author to notify.
     * @param bookTitle Title of the published book.
     */
    public void notifyAuthor(String authorEmail, String bookTitle) {
        NotificationThread notificationThread = new NotificationThread(authorEmail, bookTitle);
        notificationThread.start();  // Start notification in a separate thread to avoid blocking
    }
}
