package com.project.june.thought.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by June on 2017/4/13.
 */
@Table(name = "diary_entry")
public class DiaryEntry implements Serializable {
    @Column(name = "id", isId = true, autoGen = true)
    private java.lang.Long id;
    @Column(name = "itemId")
    private String itemId;
    @Column(name = "category")
    private String category;
    @Column(name = "date")
    private String date;
    @Column(name = "position")
    private String position;
    @Column(name = "draw")
    private String draw;
    @Column(name = "content")
    private String content;
    @Column(name = "author")
    private String author;
    @Column(name = "image")
    private String image;
    @Column(name = "number")
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
