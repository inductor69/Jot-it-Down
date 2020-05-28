package com.freejob.alert2020.jotted.model;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {

    private String mTitle;
    private Date mDate;
    private String mContent;
    private NoteType mType;

    // Constructor with title, date string, content, and type
    public Note(String title, Date date, String content, NoteType type) {
        mTitle = title;
        mContent = content;
        mType = type;
        mDate = date;
    }

    // Generated getters and setters
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getDate() { return mDate; }
    public void setDate(Date mDate) { this.mDate = mDate; }

    public String getContent() {
        return mContent;
    }
    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public NoteType getType() {
        return mType;
    }
    public void setType(NoteType type) { mType = type; }

}
