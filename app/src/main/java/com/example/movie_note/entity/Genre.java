package com.example.movie_note.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Genre implements Serializable {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;

    public Genre(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
