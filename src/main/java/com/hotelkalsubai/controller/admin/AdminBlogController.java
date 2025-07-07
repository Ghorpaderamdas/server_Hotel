package com.hotelkalsubai.controller.admin;

import com.hotelkalsubai.dto.ApiResponse;
import com.hotelkalsubai.entity.BlogPost;
import com.hotelkalsubai.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/blog")
@Tag(name = "Admin - Blog", description = "Admin blog management APIs")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping
    @Operation(summary = "Get all blog posts (Admin)", description = "Retrieve all blog posts including unpublished ones")
    public ResponseEntity<?> getAllPosts() {
        List<BlogPost> posts = blogService.getAllPosts();
        return ResponseEntity.ok(ApiResponse.success("Blog posts retrieved successfully", posts));
    }

    @PostMapping
    @Operation(summary = "Create blog post", description = "Create a new blog post")
    public ResponseEntity<?> createPost(@Valid @RequestBody BlogPost blogPost) {
        BlogPost savedPost = blogService.savePost(blogPost);
        return ResponseEntity.ok(ApiResponse.success("Blog post created successfully", savedPost));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update blog post", description = "Update an existing blog post")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @Valid @RequestBody BlogPost blogPost) {
        return blogService.getPostById(id)
                .map(existingPost -> {
                    blogPost.setId(id);
                    BlogPost updatedPost = blogService.savePost(blogPost);
                    return ResponseEntity.ok(ApiResponse.success("Blog post updated successfully", updatedPost));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete blog post", description = "Delete a blog post")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        return blogService.getPostById(id)
                .map(post -> {
                    blogService.deletePost(id);
                    return ResponseEntity.ok(ApiResponse.success("Blog post deleted successfully"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}