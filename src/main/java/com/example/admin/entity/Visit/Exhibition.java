package com.example.admin.entity.Visit;



public class Exhibition {
    private String exhibition_id;
    private String verse_id; // 展区所属的宇宙ID
    private String name; // 展区名字
    private String description; // 展区描述

    public String getExhibition_id() {
        return exhibition_id;
    }

    public void setExhibition_id(String exhibition_id) {
        this.exhibition_id = exhibition_id;
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

    public String getVerse_id() {
        return verse_id;
    }

    public void setVerse_id(String verse_id) {
        this.verse_id = verse_id;
    }

    @Override
    public String toString() {
        return "Exhibition{" +
                "exhibition_id='" + exhibition_id + '\'' +
                ", verse_id='" + verse_id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
