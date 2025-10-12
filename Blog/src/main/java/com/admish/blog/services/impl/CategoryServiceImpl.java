package com.admish.blog.services.impl;


import com.admish.blog.domain.entities.Category;
import com.admish.blog.repository.CategoryRepository;
import com.admish.blog.services.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        if (categoryRepository.existsByNameIgnoreCase(category.getName())){
            throw new IllegalArgumentException("category already exists with the name  "+category.getName());
        }
        return categoryRepository.save(category);


    }

    @Override
    public void deleteCategory(UUID id) {

        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()){

            if (!category.get().getPosts().isEmpty()){
                throw new IllegalStateException("Category has post associated with it ! ");
            }
            categoryRepository.deleteById(id);
        }
    }
}
