package com.plantcare.controller;

import com.plantcare.model.CommunityPost;
import com.plantcare.service.CommunityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * CONTROLLER — CommunityController
 * Handles the Reddit-style community feed, votes, reactions, reposts, and comments.
 */
@Controller
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @GetMapping
    public String showFeed(@RequestParam(value = "sort", defaultValue = "hot") String sort,
                           @RequestParam(value = "tag", defaultValue = "All") String tag,
                           Model model) {
        model.addAttribute("posts", communityService.getPosts(sort, tag));
        model.addAttribute("currentSort", sort.toLowerCase());
        model.addAttribute("currentTag", tag);
        model.addAttribute("tags", communityService.getAvailableTags());
        return "community/index";
    }

    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable int id, Model model, RedirectAttributes ra) {
        try {
            CommunityPost post = communityService.getById(id);
            model.addAttribute("post", post);
            return "community/detail";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Discussion post not found.");
            return "redirect:/community";
        }
    }

    @PostMapping("/add")
    public String createPost(@RequestParam("author") String author,
                             @RequestParam(value = "authorAvatar", defaultValue = "🌿") String authorAvatar,
                             @RequestParam(value = "authorBadge", defaultValue = "Gardener") String authorBadge,
                             @RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @RequestParam("tag") String tag,
                             RedirectAttributes ra) {
        try {
            CommunityPost post = communityService.createPost(author, authorAvatar, authorBadge, title, content, tag);
            ra.addFlashAttribute("success", "🌱 Your query has been posted to the community!");
            return "redirect:/community/post/" + post.getId();
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to create post: " + e.getMessage());
            return "redirect:/community";
        }
    }

    @PostMapping("/vote/{id}")
    public String votePost(@PathVariable int id,
                           @RequestParam("type") String type,
                           @RequestParam(value = "returnTo", defaultValue = "feed") String returnTo,
                           RedirectAttributes ra) {
        boolean isUpvote = "up".equalsIgnoreCase(type);
        communityService.vote(id, isUpvote);
        if ("detail".equalsIgnoreCase(returnTo)) {
            return "redirect:/community/post/" + id;
        }
        return "redirect:/community";
    }

    @PostMapping("/react/{id}")
    public String reactToPost(@PathVariable int id,
                              @RequestParam("emoji") String emoji,
                              @RequestParam(value = "returnTo", defaultValue = "feed") String returnTo) {
        communityService.react(id, emoji);
        if ("detail".equalsIgnoreCase(returnTo)) {
            return "redirect:/community/post/" + id;
        }
        return "redirect:/community";
    }

    @PostMapping("/repost/{id}")
    public String repostPost(@PathVariable int id,
                             @RequestParam(value = "returnTo", defaultValue = "feed") String returnTo,
                             RedirectAttributes ra) {
        communityService.repost(id);
        ra.addFlashAttribute("success", "🔁 Reposted to your gardening network!");
        if ("detail".equalsIgnoreCase(returnTo)) {
            return "redirect:/community/post/" + id;
        }
        return "redirect:/community";
    }

    @PostMapping("/comment/{id}")
    public String addComment(@PathVariable int id,
                             @RequestParam("author") String author,
                             @RequestParam(value = "authorBadge", defaultValue = "Plant Enthusiast") String authorBadge,
                             @RequestParam("content") String content,
                             RedirectAttributes ra) {
        try {
            communityService.addComment(id, author, authorBadge, content);
            ra.addFlashAttribute("success", "💬 Your reply was posted!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to post comment: " + e.getMessage());
        }
        return "redirect:/community/post/" + id;
    }

    @PostMapping("/comment-vote/{postId}/{commentId}")
    public String voteComment(@PathVariable int postId,
                              @PathVariable int commentId,
                              @RequestParam("type") String type) {
        communityService.voteComment(postId, commentId, "up".equalsIgnoreCase(type));
        return "redirect:/community/post/" + postId;
    }
}
