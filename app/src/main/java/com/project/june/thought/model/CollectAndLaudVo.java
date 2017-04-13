package com.project.june.thought.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by June on 2017/4/12.
 */
@Table(name = "collect_entry")
public class CollectAndLaudVo implements Serializable {
    @Column(name = "id", isId = true)
    private Long id;
    @Column(name = "itemId")
    private String itemId;
    @Column(name = "category")
    private String category;
    @Column(name = "title")
    private String title;
    @Column(name = "summary")
    private String summary;
    @Column(name = "commentNumber")
    private int commentNumber;
    @Column(name = "laudNumber")
    private int laudNumber;
    @Column(name = "isCollect")
    private Boolean isCollect;
    @Column(name = "isLaud")
    private Boolean isLaud;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

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
