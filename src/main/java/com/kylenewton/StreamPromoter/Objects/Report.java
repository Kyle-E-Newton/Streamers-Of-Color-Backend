/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamPromoter.Objects;

import com.kylenewton.StreamPromoter.Util.Platform;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Report Object
 * Adds new link when a new report is submitted
 */
@Document(collection = "reports")
public class Report {

    private String username;
    private Platform platform;
    private int reports;
    private List<String> incidentURL = new ArrayList<>();

    public Report(String username, Platform platform, String incidentURL) {
        this.username = username;
        this.platform = platform;
        this.incidentURL.add(incidentURL);
    }

    public Report(String username, Platform platform) {
        this.username = username;
        this.platform = platform;
    }

    public Report() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public int getReports() {
        return reports;
    }

    public void setReports(int reports) {
        this.reports = reports;
    }

    public List<String> getIncidentURL() {
        return incidentURL;
    }

    public void setIncidentURL(List<String> incidentURL) {
        this.incidentURL = incidentURL;
    }

    /**
     * Adds a new incident to the streamer
     * @param incidentURL   URL of the incident
     */
    public void addIncidentURL(String incidentURL) {
        this.incidentURL.add(incidentURL);
    }

    /**
     * Removes an incident from the list if deemed not a problem
     * @param incidentURL   URL to remove
     */
    public void removeIncidentURL(String incidentURL) {
        this.incidentURL.remove(incidentURL);
    }

    @Override
    public String toString() {
        return String.format("Report [Streamer: %s, Platform: %s, IncidentURL Count: %d", this.username, this.platform, this.incidentURL.size());
    }
}
