package com.hotelkalsubai.service;

import com.hotelkalsubai.entity.MenuItem;
import com.hotelkalsubai.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findByIsAvailableTrue();
    }

    public List<MenuItem> getMenuItemsByCategory(String category) {
        return menuItemRepository.findByCategoryAndIsAvailableTrue(category);
    }

    public Optional<MenuItem> getMenuItemById(Long id) {
        return menuItemRepository.findById(id);
    }

    public MenuItem saveMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }

    public List<MenuItem> getAllMenuItemsForAdmin() {
        return menuItemRepository.findAll();
    }
}