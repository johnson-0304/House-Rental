package com.fyp.houserental.domain;

public class Transaction {

    private String Description;
    private String UserId;
    private String DateTime;

    public Transaction(String description, String userId, String dateTime) {
        Description = description;
        UserId = userId;
        DateTime = dateTime;
    }

    public Transaction() {
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "Description='" + Description + '\'' +
                ", UserId='" + UserId + '\'' +
                ", DateTime='" + DateTime + '\'' +
                '}';
    }
}
