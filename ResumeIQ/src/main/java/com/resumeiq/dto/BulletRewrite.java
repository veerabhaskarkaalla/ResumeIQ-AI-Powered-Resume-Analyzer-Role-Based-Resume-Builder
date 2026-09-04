package com.resumeiq.dto;

public class BulletRewrite {

    private Integer sourceIndex;

    private String text;


    public BulletRewrite() {
    }


    public Integer getSourceIndex() {
        return sourceIndex;
    }


    public void setSourceIndex(
            Integer sourceIndex) {

        this.sourceIndex = sourceIndex;
    }


    public String getText() {
        return text;
    }


    public void setText(
            String text) {

        this.text = text;
    }
}