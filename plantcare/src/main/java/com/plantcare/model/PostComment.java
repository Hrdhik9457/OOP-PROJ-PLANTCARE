package com.plantcare.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * ENTITY — PostComment
 *
 * OOP Concept: Composition & Encapsulation
 * Represents a community reply or answer to a plant query.
 */
public class PostComment {

    private int id;
    private int postId;
    private String author;
    private String authorBadge;
    private String content;
    private LocalDateTime createdAt;
    private int upvotes;

    public PostComment() {
        this.createdAt = LocalDateTime.now();
        this.upvotes = 1;
    }

    public PostComment(int id, int postId, String author, String authorBadge, String content) {
        this.id = id;
        this.postId = postId;
        this.author = author != null ? author : "Gardener";
        this.authorBadge = authorBadge != null ? authorBadge : "Enthusiast";
        this.content = content != null ? content : "";
        this.createdAt = LocalDateTime.now();
        this.upvotes = 1;
    }

    public void upvote() {
        this.upvotes++;
    }

    public void downvote() {
        this.upvotes--;
    }

    public String getFormattedTimeAgo() {
        if (createdAt == null) return "just now";
        long mins = ChronoUnit.MINUTES.between(createdAt, LocalDateTime.now());
        if (mins < 1) return "just now";
        if (mins < 60) return mins + "m ago";
        long hours = ChronoUnit.HOURS.between(createdAt, LocalDateTime.now());
        if (hours < 24) return hours + "h ago";
        long days = ChronoUnit.DAYS.between(createdAt, LocalDateTime.now());
        return days + "d ago";
    }

    // ── Getters & Setters ──────────────────────────────────────────────────
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getAuthorBadge() { return authorBadge; }
    public void setAuthorBadge(String authorBadge) { this.authorBadge = authorBadge; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getUpvotes() { return upvotes; }
    public void setUpvotes(int upvotes) { this.upvotes = upvotes; }
}
