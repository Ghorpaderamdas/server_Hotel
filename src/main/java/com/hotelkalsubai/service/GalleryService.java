package com.hotelkalsubai.service;

import com.hotelkalsubai.entity.GalleryImage;
import com.hotelkalsubai.repository.GalleryImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GalleryService {

    @Autowired
    private GalleryImageRepository galleryImageRepository;

    public List<GalleryImage> getAllImages() {
        return galleryImageRepository.findAllByOrderByUploadedAtDesc();
    }

    public List<GalleryImage> getImagesByCategory(String category) {
        return galleryImageRepository.findByCategory(category);
    }

    public Optional<GalleryImage> getImageById(Long id) {
        return galleryImageRepository.findById(id);
    }

    public GalleryImage saveImage(GalleryImage galleryImage) {
        return galleryImageRepository.save(galleryImage);
    }

    public void deleteImage(Long id) {
        galleryImageRepository.deleteById(id);
    }
}