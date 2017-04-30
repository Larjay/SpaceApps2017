package com.example.android.codefoo2017.pullapi;

public class Video {

    /* Metadata */
    private String name;
    private String description;
    private String thumbnail;
    private String publishDate;
    private long publishDateLong;
    private String longTitle;
    private int duration;
    private String url;
    private String slug;
    private String networks;
    private String state;


    public Video() {
        // Empty Constructor
    }

    public void printJava() {

        System.out.println("Thumbnail: " + thumbnail);
        System.out.println("Name: " + name);
        System.out.println("Description: " + description);
        System.out.println("Publish Date: " + publishDate);
        System.out.println("Long Title: " + longTitle);
        System.out.println("Duration: " + duration);
        System.out.println("Url: " + url);
        System.out.println("Slug: " + slug);
        System.out.println("Networks: " + networks);
        System.out.println("State: " + state);
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getLongTitle() {
        return longTitle;
    }

    public void setLongTitle(String longTitle) {
        this.longTitle = longTitle;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getNetworks() {
        return networks;
    }

    public void setNetworks(String networks) {
        this.networks = networks;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPublishDateLong(String publishDate) {

        this.publishDate = publishDate;
        String dashesStripped = publishDate.replace("-", "");
        String allStripped = dashesStripped.replace(":", "");

        int indexTime = allStripped.indexOf("T");
        int indexPlus = allStripped.indexOf("+");
        String firstPart = allStripped.substring(0, indexTime);
        String secondPart = allStripped.substring(indexTime + 1, indexPlus);
        try {
            publishDateLong = Long.valueOf(firstPart + secondPart);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
    }

    /**
     * Accessor for the publish date as an int
     * @return integer of the publish date
     */
    public long getPublishDateLong() {
        return publishDateLong;
    }

}
