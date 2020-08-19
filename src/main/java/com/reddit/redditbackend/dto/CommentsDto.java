package com.reddit.redditbackend.dto;

import java.time.Instant;

public class CommentsDto {
    private Long id;
    private Long postId;
    private Instant createDate;
    private String text;
    private String username;

    public CommentsDto() {
    }

    public CommentsDto(Long id, Long postId, Instant createDate, String text, String username) {
        this.id = id;
        this.postId = postId;
        this.createDate = createDate;
        this.text = text;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
