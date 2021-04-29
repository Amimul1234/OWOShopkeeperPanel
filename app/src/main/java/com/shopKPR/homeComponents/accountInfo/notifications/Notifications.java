package com.shopKPR.homeComponents.accountInfo.notifications;

public class Notifications {
    private Long notificationId;
    private String notificationName;
    private String notificationImage;

    public Notifications() {
    }

    public Notifications(Long notificationId, String notificationName,
                         String notificationImage) {
        this.notificationId = notificationId;
        this.notificationName = notificationName;
        this.notificationImage = notificationImage;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    public String getNotificationImage() {
        return notificationImage;
    }

    public void setNotificationImage(String notificationImage) {
        this.notificationImage = notificationImage;
    }
}
