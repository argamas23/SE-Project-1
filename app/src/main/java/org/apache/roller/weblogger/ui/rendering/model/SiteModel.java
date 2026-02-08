package org.apache.roller.weblogger.ui.rendering.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SiteModel {
    private String name;
    private String description;
    private List<String> tags;
    private SiteType siteType;
    private int numberOfPosts;
    private int numberOfComments;

    public SiteModel(String name, String description, List<String> tags, SiteType siteType) {
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.siteType = siteType;
        this.numberOfPosts = 0;
        this.numberOfComments = 0;
    }

    public void initialize(Map<String, Object> data) {
        if (data.containsKey("numberOfPosts")) {
            this.numberOfPosts = (int) data.get("numberOfPosts");
        }
        if (data.containsKey("numberOfComments")) {
            this.numberOfComments = (int) data.get("numberOfComments");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public SiteType getSiteType() {
        return siteType;
    }

    public void setSiteType(SiteType siteType) {
        this.siteType = siteType;
    }

    public int getNumberOfPosts() {
        return numberOfPosts;
    }

    public void setNumberOfPosts(int numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    private enum SiteType {
        BLOG(1),
        PORTFOLIO(2),
        WIKI(3);

        private final int value;

        SiteType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static SiteType fromValue(int value) {
            for (SiteType siteType : SiteType.values()) {
                if (siteType.getValue() == value) {
                    return siteType;
                }
            }
            throw new IllegalArgumentException("Invalid site type value");
        }
    }
}