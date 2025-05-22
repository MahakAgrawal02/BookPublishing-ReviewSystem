package com.bookstore.services;

public class NotificationThread extends Thread {
    private String authorEmail;
    private String bookTitle;

    public NotificationThread(String authorEmail, String bookTitle) {
        this.authorEmail = authorEmail;
        this.bookTitle = bookTitle;
    }

    @Override
    public void run() {
        try {
            // Simulate network delay
            Thread.sleep(2000);
            System.out.println("Notification sent to " + authorEmail + ": Your book '" + bookTitle + "' has a new review.");
        } catch (InterruptedException e) {
            System.err.println("Notification thread interrupted: " + e.getMessage());
        }
    }
} 