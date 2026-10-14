package com.plantcare.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ENTITY — CommunityPost
 *
 * OOP Concepts demonstrated:
 *   - Composition: Contains a List<PostComment> and Map<String, Integer> of reactions.
 *   - Encapsulation: Internal vote tallies and score formulas managed via public methods.
 */
public class CommunityPost {

    private int id;
    private String author;
    private String authorAvatar;
    private String authorBadge;
    private String title;
    private String content;
    private String tag;
    private LocalDateTime createdAt;
    private int upvotes;
    private int downvotes;
    private int reposts;

    // COMPOSITION: Map of reaction emojis to count
    private final Map<String, Integer> reactions = new LinkedHashMap<>();

    // COMPOSITION: List of comments / replies
    private final List<PostComment> comments = new ArrayList<>();

    public CommunityPost() {
        this.createdAt = LocalDateTime.now();
        this.authorAvatar = "🌿";
        this.authorBadge = "Gardener";
        initDefaultReactions();
    }

    public CommunityPost(int id, String author, String authorAvatar, String authorBadge,
                         String title, String content, String tag, LocalDateTime createdAt,
                         int initialUpvotes) {
        this.id = id;
        this.author = author != null ? author : "AnonymousGardener";
        this.authorAvatar = authorAvatar != null ? authorAvatar : "🌿";
        this.authorBadge = authorBadge != null ? authorBadge : "Gardener";
        this.title = title != null ? title : "Plant Query";
        this.content = content != null ? content : "";
        this.tag = tag != null ? tag : "General";
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.upvotes = Math.max(1, initialUpvotes);
        this.downvotes = 0;
        this.reposts = 0;
        initDefaultReactions();
    }

    private void initDefaultReactions() {
        reactions.put("🌿", 0);
        reactions.put("💚", 0);
        reactions.put("💡", 0);
        reactions.put("🌱", 0);
    }

    // ── Domain Methods ─────────────────────────────────────────────────────

    public int getScore() {
        return upvotes - downvotes;
    }

    public void upvote() {
        this.upvotes++;
    }

    public void downvote() {
        this.downvotes++;
    }

    public void incrementReposts() {
        this.reposts++;
    }

    public void addReaction(String emoji) {
        reactions.put(emoji, reactions.getOrDefault(emoji, 0) + 1);
    }

    public int getTotalReactions() {
        return reactions.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void addComment(PostComment comment) {
        this.comments.add(comment);
    }

    public int getCommentCount() {
        return comments.size();
    }

    /**
     * Reddit-style "Hot" algorithm:
     * Combines net score, total comments, and penalizes older posts logarithmically.
     */
    public double getHotScore() {
        long hoursAge = Math.max(1, ChronoUnit.HOURS.between(createdAt, LocalDateTime.now()));
        double activity = getScore() + (comments.size() * 2.5) + (getTotalReactions() * 1.2);
        return activity / Math.pow(hoursAge + 2, 1.25);
    }

    /**
     * "Trending" algorithm:
     * High velocity of recent comments, reactions, and reposts.
     */
    public double getTrendingScore() {
        long hoursAge = Math.max(1, ChronoUnit.HOURS.between(createdAt, LocalDateTime.now()));
        double velocity = (comments.size() * 3.0) + (reposts * 2.0) + getTotalReactions();
        return velocity / Math.sqrt(hoursAge);
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

    public String getTagBadgeClass() {
        return switch (tag) {
            case "Pest & Disease"     -> "badge-red";
            case "Indoor Care"        -> "badge-blue";
            case "Soil & Fertilizer"  -> "badge-yellow";
            case "Propagation"        -> "badge-green";
            case "Plant ID"           -> "badge-purple";
            default                   -> "badge-green";
        };
    }

    // ── Getters and Setters ────────────────────────────────────────────────
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getAuthorAvatar() { return authorAvatar; }
    public void setAuthorAvatar(String authorAvatar) { this.authorAvatar = authorAvatar; }

    public String getAuthorBadge() { return authorBadge; }
    public void setAuthorBadge(String authorBadge) { this.authorBadge = authorBadge; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getUpvotes() { return upvotes; }
    public void setUpvotes(int upvotes) { this.upvotes = upvotes; }

    public int getDownvotes() { return downvotes; }
    public void setDownvotes(int downvotes) { this.downvotes = downvotes; }

    public int getReposts() { return reposts; }
    public void setReposts(int reposts) { this.reposts = reposts; }

    public Map<String, Integer> getReactions() { return reactions; }
    public List<PostComment> getComments() { return comments; }
}
