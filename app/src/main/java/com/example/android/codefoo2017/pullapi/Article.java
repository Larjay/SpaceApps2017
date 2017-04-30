package com.example.android.codefoo2017.pullapi;

public class Article {

    private String thumbnail;
    private String headline;
    private String networks;
    private String state;
    private String slug;
    private String subHeadline;
    private String publishDate;
    private long publishDateLong;
    private String articleType;
    private String url;

    public Article() {
        url = "http://ign.com";
    }

    /**
     * Prints article object's content to system out for Java
     */
    public void printJava() {
        System.out.println("Thumbnail: " + thumbnail);
        System.out.println("Headline: " + headline);
        System.out.println("Networks: " + networks);
        System.out.println("State: " + state);
        System.out.println("Slug: " + slug);
        System.out.println("Sub Headline: " + subHeadline);
        System.out.println("Publish Date: " + publishDate);
        System.out.println("Article Type: " + articleType + "\n");
    }

    /**
     * Accessor for the thumbnail for the article
     * @return string of the thumbnail url
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * Mutator for the thumbnail string for the article
     * @param thumbnail url string of thumbnail for article
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * Accessor for the headline string for the article
     * @return string of headline of article
     */
    public String getHeadline() {
        return headline;
    }

    /**
     * Mutator for the headline for an article
     * @param headline string of an article headline
     */
    public void setHeadline(String headline) {
        this.headline = headline;
    }

    /**
     * Accessor for the networks of an article
     * @return string of networks article is in
     */
    public String getNetworks() {
        return networks;
    }

    /**
     * Mutator for the networks of an article
     * @param networks string of networks an article is in
     */
    public void setNetworks(String networks) {
        this.networks = networks;
    }

    /**
     * Accessor for state of an article
     * @return state string of an article
     */
    public String getState() {
        return state;
    }

    /**
     * Mutator for the state of an article
     * @param state string of the state for an article
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Accessor for the slug of an article
     * @return slug string of the slug of an article
     */
    public String getSlug() {
        return slug;
    }

    /**
     * Mutator for the slug of an article
     * @param slug string of the slug of an article
     */
    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     * Accessor for the sub headline of an article
     * @return string for the sub headline of an article
     */
    public String getSubHeadline() {
        return subHeadline;
    }

    /**
     * Mutator for the sub headline of an article
     * @param subHeadline string of the sub headline of an article
     */
    public void setSubHeadline(String subHeadline) {
        this.subHeadline = subHeadline;
    }

    /**
     * Accessor for the publish date of an article
     * @return string of the published date
     */
    public String getPublishDate() {
        return publishDate;
    }

    /**
     * Mutator for the publish date of an article
     * @param publishDate string of the publish date of an article
     */
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    /**
     * Accessor of the article type of an article
     * @return string of the article type of an article
     */
    public String getArticleType() {
        return articleType;
    }

    /**
     * Mutator for the article type of an article
     * @param articleType string of the article type of an article
     */
    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    /**
     * Accessor for the url of an article
     * @return string of the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Mutator for the url of an article
     * Uses the slug to build the proper url as a String
     */
    public void setUrl() {

        String articles = "/articles/";
        String year, month, day;
        int indexFirst, indexSecond, indexThird;

        indexFirst = publishDate.indexOf("-");
        year = publishDate.substring(0, indexFirst);

        indexSecond = publishDate.indexOf("-", indexFirst + 1);
        month = publishDate.substring(indexFirst + 1, indexSecond);

        indexThird = publishDate.indexOf("T");
        day = publishDate.substring(indexSecond + 1, indexThird);
        this.url += articles + year + "/" + month + "/" + day + "/" + slug;
        System.out.println(this.url);
    }

    /**
     * Mutator for a publish date - converts from string to int
     * @param publishDate string of the publish date
     */
    public void setPublishDateLong(String publishDate) {

        this.publishDate = publishDate;
        String dashesStripped = publishDate.replace("-", "");
        String allStripped = dashesStripped.replace(":", "");

        int indexTime = allStripped.indexOf("T");
        int indexPlus = allStripped.indexOf("+");
        String firstPart = allStripped.substring(0, indexTime);
        String secondPart = allStripped.substring(indexTime + 1, indexPlus);
        System.out.println(firstPart + secondPart);
        try {
            publishDateLong = Long.parseLong(firstPart + secondPart);
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
