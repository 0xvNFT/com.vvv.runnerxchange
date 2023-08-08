package com.vvv.runnerxchange;

import java.io.Serializable;

public class HistoryItem implements Serializable {
    private final String dateTime;
    private final String duration;
    private final String distance;
    private final String calories;

    public HistoryItem(String dateTime, String duration, String distance, String calories) {
        this.dateTime = dateTime;
        this.duration = duration;
        this.distance = distance;
        this.calories = calories;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getDuration() {
        return duration;
    }

    public String getDistance() {
        return distance;
    }

    public String getCaloriesBurned() {
        return calories;
    }
}
