package org.example.mvcdemo.service;

import org.example.mvcdemo.exception.BusinessException;
import org.example.mvcdemo.entity.Category;
import org.example.mvcdemo.repository.CategoryRepository;

import java.util.List;

public class CategoryService {

    private final CategoryRepository categoryRepository = new CategoryRepository();

    public List<Category> findActive() {
        return categoryRepository.findActive();
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(int id) {
        Category c = categoryRepository.findById(id);
        if (c == null) {
            throw new BusinessException("Khong tim thay danh muc id = " + id);
        }
        return c;
    }

    public void create(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("Ten danh muc khong duoc rong.");
        }
        if (categoryRepository.existsByName(name, null)) {
            throw new BusinessException("Ten danh muc da ton tai.");
        }
        Category c = new Category();
        c.setName(name.trim());
        c.setDescription(blankToNull(description));
        c.setStatus(true);
        categoryRepository.insert(c);
    }

    public void update(int id, String name, String description, boolean status) {
        Category existing = categoryRepository.findById(id);
        if (existing == null) {
            throw new BusinessException("Khong tim thay danh muc id = " + id);
        }
        if (name == null || name.isBlank()) {
            throw new BusinessException("Ten danh muc khong duoc rong.");
        }
        if (categoryRepository.existsByName(name, id)) {
            throw new BusinessException("Ten danh muc da ton tai.");
        }
        if (!status && existing.isStatus()) {
            long active = categoryRepository.countActiveProducts(id);
            if (active > 0) {
                throw new BusinessException(
                        "Khong the ngung danh muc khi con " + active + " san pham dang ban.");
            }
        }
        existing.setName(name.trim());
        existing.setDescription(blankToNull(description));
        existing.setStatus(status);
        categoryRepository.update(existing);
    }

    private static String blankToNull(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }
}
