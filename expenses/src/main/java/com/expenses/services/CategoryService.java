package com.expenses.services;

import com.expenses.DTOs.CategoryDTO;
import com.expenses.entities.Category;
import com.expenses.entities.TransactionType;
import com.expenses.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    final private CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public List<CategoryDTO> getCategories(TransactionType type, Boolean regular) {
        List<Category> categories;
        if(regular!=null){
            categories = categoryRepository.findAllByTypeAndRegular(type, regular);
        }
        else{
            categories = categoryRepository.findAllByType(type);
        }

        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for(Category category : categories){
            if(category.getCustom() == false) {
                categoryDTOList.add(new CategoryDTO(category.getId(), category.getType(), category.getName(), category.getCustom()));
            }
        }
        return categoryDTOList;
    }

    public CategoryDTO get(int categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException(""));
        return new CategoryDTO(category.getId(), category.getType(), category.getName(), category.getCustom());
    }

}
