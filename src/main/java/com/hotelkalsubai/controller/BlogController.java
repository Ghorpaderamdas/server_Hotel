package com.hotelkalsubai.controller;

import com.hotelkalsubai.dto.ApiResponse;
import com.hotelkalsubai.entity.BlogPost;
import com.hotelkalsubai.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/blog")
@Tag(name = "Blog", description = "Blog management APIs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping
    @Operation(summary = "Get all published blog posts", description = "Retrieve all published blog posts")
    public ResponseEntity<?> getAllPublishedPosts() {
        List<BlogPost> posts = blogService.getAllPublishedPosts();
        return ResponseEntity.ok(ApiResponse.success("Blog posts retrieved successfully", posts));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get blog post by ID", description = "Retrieve a specific blog post by its ID")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        return blogService.getPostById(id)
                .map(post -> ResponseEntity.ok(ApiResponse.success("Blog post found", post)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @Operation(summary = "Search blog posts", description = "Search blog posts by title")
    public ResponseEntity<?> searchPosts(@RequestParam String title) {
        List<BlogPost> posts = blogService.searchPosts(title);
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", posts));
    }
}