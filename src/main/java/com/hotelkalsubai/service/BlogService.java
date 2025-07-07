package com.hotelkalsubai.service;

import com.hotelkalsubai.entity.BlogPost;
import com.hotelkalsubai.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    public List<BlogPost> getAllPublishedPosts() {
        return blogPostRepository.findByIsPublishedTrueOrderByCreatedAtDesc();
    }

    public Optional<BlogPost> getPostById(Long id) {
        return blogPostRepository.findById(id);
    }

    public BlogPost savePost(BlogPost blogPost) {
        return blogPostRepository.save(blogPost);
    }

    public void deletePost(Long id) {
        blogPostRepository.deleteById(id);
    }

    public List<BlogPost> getAllPosts() {
        return blogPostRepository.findAll();
    }

    public List<BlogPost> searchPosts(String title) {
        return blogPostRepository.findByTitleContainingIgnoreCase(title);
    }
}