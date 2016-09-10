/**
 * CalendarEvent.java
 * <p/>
 * A model that represents the calendar event.
 *
 * @category Global Analytics
 * @package com.globalanalytics.dataapp.data.model
 * @version 1.0
 * @author Rajkumar.N
 * @copyright Copyright (C) 2016 Global Analytics. All rights reserved.
 */
package com.oozmakappa.oyeloans.DataExtraction.model;

import com.google.gson.annotations.SerializedName;

/**
 * A model that represents the calendar event.
 */
public class CalendarEvent {

    private String title;

    @SerializedName("start_date")
    private String startDate;

    @SerializedName("end_date")
    private String endDate;
    private String description;

    @Override
    public String toString() {
        return "CalendarEvent [ title = " + title + ", startDate = " + startDate + ", endDate = " + endDate + ", description = " + description + "]";
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets start date.
     *
     * @return the start date
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets start date.
     *
     * @param startDate the start date
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets end date.
     *
     * @return the end date
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets end date.
     *
     * @param endDate the end date
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
