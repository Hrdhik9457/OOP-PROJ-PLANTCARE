package com.plantcare.service;

import com.plantcare.model.CommunityPost;
import com.plantcare.model.PostComment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * SERVICE — CommunityService
 *
 * OOP Concepts demonstrated:
 *   - Collections: Manages ArrayList<CommunityPost> in memory.
 *   - Polymorphic Sorting & Strategy: Implements Reddit-like Hot, New, Top, and Trending algorithms.
 *   - Composition Management: Mutates post reactions, comments, and votes.
 */
@Service
public class CommunityService {

    private final List<CommunityPost> posts = new ArrayList<>();
    private int nextPostId = 1;
    private int nextCommentId = 1;

    public CommunityService() {
        seedSampleCommunityFeed();
    }

    public List<CommunityPost> getPosts(String sort, String tagFilter) {
        var stream = posts.stream();

        // Optional Tag Filter
        if (tagFilter != null && !tagFilter.isBlank() && !tagFilter.equalsIgnoreCase("All")) {
            stream = stream.filter(p -> p.getTag().equalsIgnoreCase(tagFilter));
        }

        // Reddit-style sorting
        return switch (sort != null ? sort.toLowerCase() : "hot") {
            case "new" -> stream.sorted(Comparator.comparing(CommunityPost::getCreatedAt).reversed()).toList();
            case "top" -> stream.sorted(Comparator.comparingInt(CommunityPost::getScore).reversed()).toList();
            case "trending" -> stream.sorted(Comparator.comparingDouble(CommunityPost::getTrendingScore).reversed()).toList();
            default -> stream.sorted(Comparator.comparingDouble(CommunityPost::getHotScore).reversed()).toList();
        };
    }

    public Optional<CommunityPost> findById(int id) {
        return posts.stream().filter(p -> p.getId() == id).findFirst();
    }

    public CommunityPost getById(int id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id " + id));
    }

    public CommunityPost createPost(String author, String authorAvatar, String authorBadge,
                                   String title, String content, String tag) {
        CommunityPost post = new CommunityPost(
                nextPostId++,
                author,
                authorAvatar,
                authorBadge,
                title,
                content,
                tag,
                LocalDateTime.now(),
                1
        );
        posts.add(0, post); // Prepend to top
        return post;
    }

    public void vote(int postId, boolean isUpvote) {
        findById(postId).ifPresent(post -> {
            if (isUpvote) post.upvote();
            else post.downvote();
        });
    }

    public void react(int postId, String emoji) {
        findById(postId).ifPresent(post -> post.addReaction(emoji));
    }

    public void repost(int postId) {
        findById(postId).ifPresent(CommunityPost::incrementReposts);
    }

    public PostComment addComment(int postId, String author, String authorBadge, String content) {
        CommunityPost post = getById(postId);
        PostComment comment = new PostComment(nextCommentId++, postId, author, authorBadge, content);
        post.addComment(comment);
        return comment;
    }

    public void voteComment(int postId, int commentId, boolean isUpvote) {
        findById(postId).ifPresent(post -> {
            post.getComments().stream()
                    .filter(c -> c.getId() == commentId)
                    .findFirst()
                    .ifPresent(c -> {
                        if (isUpvote) c.upvote();
                        else c.downvote();
                    });
        });
    }

    public List<String> getAvailableTags() {
        return List.of("All", "Indoor Care", "Pest & Disease", "Soil & Fertilizer", "Propagation", "Plant ID");
    }

    private void seedSampleCommunityFeed() {
        LocalDateTime now = LocalDateTime.now();

        // ── Post 1: Monstera yellow leaf query (Hot & Popular)
        CommunityPost p1 = new CommunityPost(nextPostId++, "SproutNewbie", "🪴", "Beginner",
                "Why are my Monstera deliciosa lower leaf tips turning yellow and soft?",
                "I bought this Monstera 3 weeks ago. Over the last 4 days, two of the lowest leaves started turning pale yellow with brown mushy tips. I water it every 3 days. Is this root rot or just normal acclimation?",
                "Indoor Care", now.minusHours(3), 42);
        p1.setDownvotes(2);
        p1.setReposts(6);
        p1.addReaction("🌿"); p1.addReaction("🌿"); p1.addReaction("🌿");
        p1.addReaction("💡"); p1.addReaction("💡");
        p1.addReaction("💚");

        PostComment c1_1 = new PostComment(nextCommentId++, p1.getId(), "BotanistDave", "Master Gardener",
                "Watering every 3 days is almost certainly too frequent for an indoor Monstera! The top 2 inches of soil must dry out between waterings. Gently lift the nursery pot to check for dark, mushy roots.");
        c1_1.setUpvotes(18);
        PostComment c1_2 = new PostComment(nextCommentId++, p1.getId(), "FloraFanatic", "Plant Doctor",
                "Seconding Dave! Make sure your pot has drainage holes. Never let it sit in standing runoff saucer water.");
        c1_2.setUpvotes(9);
        p1.addComment(c1_1);
        p1.addComment(c1_2);
        posts.add(p1);

        // ── Post 2: Fiddle leaf fig question (Trending)
        CommunityPost p2 = new CommunityPost(nextPostId++, "UrbanJungleGal", "🌳", "Houseplant Pro",
                "PSA & Question: Fiddle Leaf Fig dropping leaves after moving apartments — best recovery strategy?",
                "Moved apartments last weekend and my 5ft Fiddle Leaf Fig has dropped 4 large healthy green leaves with zero discoloration. I know they hate change, but should I hold back water or mist the foliage?",
                "Indoor Care", now.minusHours(7), 89);
        p2.setDownvotes(3);
        p2.setReposts(14);
        p2.addReaction("💚"); p2.addReaction("💚"); p2.addReaction("💚"); p2.addReaction("💚");
        p2.addReaction("🌱"); p2.addReaction("🌱");
        PostComment c2_1 = new PostComment(nextCommentId++, p2.getId(), "GreenThumb99", "Enthusiast",
                "Classic FLF shock! Keep it in one spot with bright indirect light. Don't over-fertilize or move it around, and it will stabilize in 2-3 weeks.");
        c2_1.setUpvotes(24);
        p2.addComment(c2_1);
        posts.add(p2);

        // ── Post 3: Fungus gnats pest inquiry
        CommunityPost p3 = new CommunityPost(nextPostId++, "SoilSurfer", "🪰", "Collector",
                "Tiny flying bugs all over my potting soil! Are these fungus gnats and how do I kill them naturally?",
                "Every time I water my snake plant and pothos, clouds of tiny black flies scatter out of the soil. Yellow sticky traps catch some, but how do I eliminate the larvae inside the soil without harsh chemicals?",
                "Pest & Disease", now.minusHours(14), 65);
        p3.setDownvotes(1);
        p3.setReposts(8);
        p3.addReaction("💡"); p3.addReaction("💡"); p3.addReaction("💡");
        p3.addReaction("🌿");
        PostComment c3_1 = new PostComment(nextCommentId++, p3.getId(), "OrganicSam", "Eco-Grower",
                "Mosquito Dunks / BTI (Bacillus thuringiensis israelensis) dissolved in your watering can will eradicate the larvae in 1-2 waterings without hurting the plant.");
        c3_1.setUpvotes(31);
        p3.addComment(c3_1);
        posts.add(p3);

        // ── Post 4: DIY Aroid Soil Mix Recipe
        CommunityPost p4 = new CommunityPost(nextPostId++, "RootMaster", "🧪", "Botanist",
                "My ultimate chunky Aroid potting mix recipe that solved all root rot issues forever",
                "Here is my standard recipe: 40% chunky pine bark chips, 25% coarse perlite or pumice, 20% coco coir, 10% worm castings, 5% horticultural charcoal. Super airy, drains in seconds, roots breathe happily!",
                "Soil & Fertilizer", now.minusDays(1), 134);
        p4.setDownvotes(4);
        p4.setReposts(29);
        p4.addReaction("🌿"); p4.addReaction("🌿"); p4.addReaction("🌿"); p4.addReaction("🌿");
        p4.addReaction("💚"); p4.addReaction("💚");
        p4.addReaction("💡"); p4.addReaction("💡"); p4.addReaction("💡");
        posts.add(p4);

        // ── Post 5: Tomato blossom end rot
        CommunityPost p5 = new CommunityPost(nextPostId++, "VeggieKing", "🍅", "Outdoor Gardener",
                "Black sunken patches on the bottom of my cherry tomatoes — calcium deficiency or irregular watering?",
                "First time growing tomatoes in raised outdoor planters. The green tomatoes look great until the bottom turns dark sunken leathery black. Is eggshell powder enough to fix it?",
                "Pest & Disease", now.minusDays(2), 27);
        p5.setDownvotes(0);
        p5.setReposts(3);
        p5.addReaction("💡");
        PostComment c5_1 = new PostComment(nextCommentId++, p5.getId(), "GardenGramps", "Heritage Grower",
                "Classic Blossom End Rot! While technically caused by calcium deficiency, 90% of the time the soil has plenty of calcium but irregular watering prevents root uptake. Keep soil consistently moist like a wrung-out sponge.");
        c5_1.setUpvotes(15);
        p5.addComment(c5_1);
        posts.add(p5);
    }
}
