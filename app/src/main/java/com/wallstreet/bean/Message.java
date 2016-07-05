package com.wallstreet.bean;

import java.util.ArrayList;

public class Message {
    public String id;
    public String authorId;
    public String title;
    public String summary;
    public String content;
    public String image;
    public String imageType;
    public String url;
    public String source;
    public boolean liked;
    public int likeCount;
    public int style;// MessageStyleShort 1 MessageStyleLong 2  MessageStyleUrl 3
    public int type;//1 new //2 hot
    public long createdAt;
    //public String[] subjectIds;
    public ArrayList<Stock> stocks;
    public String shareUrl;

    public Message() {
    }

    public Message(String authorId, String content, long createdAt, String id, String image,
                   String imageType, int likeCount, boolean liked, String shareUrl, String source,
                   ArrayList<Stock> stocks, int style, String summary, String title, int type, String url) {
        this.authorId = authorId;
        this.content = content;
        this.createdAt = createdAt;
        this.id = id;
        this.image = image;
        this.imageType = imageType;
        this.likeCount = likeCount;
        this.liked = liked;
        this.shareUrl = shareUrl;
        this.source = source;
        this.stocks = stocks;
        this.style = style;
        this.summary = summary;
        this.title = title;
        this.type = type;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Message{" +
                "authorId='" + authorId + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", imageType='" + imageType + '\'' +
                ", url='" + url + '\'' +
                ", source='" + source + '\'' +
                ", liked=" + liked +
                ", likeCount=" + likeCount +
                ", style=" + style +
                ", type=" + type +
                ", createdAt=" + createdAt +
                ", stocks=" + stocks +
                ", shareUrl='" + shareUrl + '\'' +
                '}';
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(ArrayList<Stock> stocks) {
        this.stocks = stocks;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
