package com.dd7.yahn.rest.client.model;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Item implements Serializable{

    private int id;
    private boolean deleted;
    private String type;
    private String by;
    private long time;
    private String timeFormatted;
    private String text;
    private boolean dead;
    private int parent;
    private List<Integer> kids;
    private String url;
    private int score;
    private String title;

    public Item(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public List<Integer> getKids() {
        return kids;
    }

    public void setKids(List<Integer> kids) {
        this.kids = kids;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeFormatted() {
        if (timeFormatted == null) {
            Date date = new Date(System.currentTimeMillis() - (time * 1000L));
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int hours = cal.get(Calendar.HOUR_OF_DAY);
            timeFormatted = String.format(Locale.ENGLISH, "%d hours ago", hours);
        }
        return timeFormatted;
    }

    public String getUrlDomainName() {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (URISyntaxException | NullPointerException e) {
            return "-";
        }
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", deleted=" + deleted +
                ", type='" + type + '\'' +
                ", by='" + by + '\'' +
                ", time=" + time +
                ", text='" + text + '\'' +
                ", dead=" + dead +
                ", parent=" + parent +
                ", kids=" + kids +
                ", url='" + url + '\'' +
                ", score=" + score +
                ", title='" + title + '\'' +
                '}';
    }
}
