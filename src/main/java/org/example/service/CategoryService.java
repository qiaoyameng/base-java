package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Category;
import org.example.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> findEnabled() {
        return categoryRepository.findByEnabledOrderBySortOrderAsc(true);
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(Long id, Category category) {
        Category existing = findById(id);
        if (existing == null) {
            return null;
        }
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        existing.setIcon(category.getIcon());
        existing.setSortOrder(category.getSortOrder());
        existing.setEnabled(category.getEnabled());
        return categoryRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
