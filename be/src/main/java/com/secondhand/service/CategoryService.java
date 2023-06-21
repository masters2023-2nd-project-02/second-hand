package com.secondhand.service;

import com.secondhand.domain.categorie.Category;
import com.secondhand.domain.categorie.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> readAll() {
        return categoryRepository.findAll();
    }
}