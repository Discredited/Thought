package com.project.june.thought.model;

import java.io.Serializable;

/**
 * Created by June on 2017/4/12.
 */

public class CollectAndLaudVo implements Serializable{

    private String title;
    private String summary;
    private int commentNumber;
    private int laudNumber;
    private Boolean isCollect;
    private Boolean isLaud;

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public int getLaudNumber() {
        return laudNumber;
    }

    public void setLaudNumber(int laudNumber) {
        this.laudNumber = laudNumber;
    }

    public Boolean getCollect() {
        return isCollect;
    }

    public void setCollect(Boolean collect) {
        isCollect = collect;
    }

    public Boolean getLaud() {
        return isLaud;
    }

    public void setLaud(Boolean laud) {
        isLaud = laud;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
