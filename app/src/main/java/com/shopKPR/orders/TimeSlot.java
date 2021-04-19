package com.shopKPR.orders;

public class TimeSlot
{
    private Long timeSlotId;
    private String timeSlotString;

    public TimeSlot() {
    }

    public TimeSlot(Long timeSlotId, String timeSlotString) {
        this.timeSlotId = timeSlotId;
        this.timeSlotString = timeSlotString;
    }

    public Long getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Long timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public String getTimeSlotString() {
        return timeSlotString;
    }

    public void setTimeSlotString(String timeSlotString) {
        this.timeSlotString = timeSlotString;
    }
}