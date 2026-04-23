package com.ecates.carrent.service.impl;

import com.ecates.carrent.dto.request.CategoryRequestDto;
import com.ecates.carrent.dto.response.CategoryResponseDto;
import com.ecates.carrent.entity.Brand;
import com.ecates.carrent.entity.Category;
import com.ecates.carrent.mapper.CategoryMapper;
import com.ecates.carrent.repository.CategoryRepository;
import com.ecates.carrent.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    private boolean existsByName (String name){

        return categoryRepository.existsByNameIgnoreCase(name);
    }

    private Category findCategoryByIdOrThrow(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Belirtilen id'ye sahip kategori bulunamadı."));
    }



    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        String categoryName = categoryRequestDto.getName().trim();

        if (existsByName(categoryName)){
            throw new RuntimeException("Kategory mevcut");
        }
        Category category = categoryMapper.toEntity(categoryRequestDto);
        category.setName(categoryName);

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryResponseDto(savedCategory);
    }





    @Override
    public List<CategoryResponseDto> getAllCategory() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryMapper.toCategoryResponseDtoList(categoryList);
    }



    @Override
    public CategoryResponseDto getByIdCategory(Long id) {
        Category category = findCategoryByIdOrThrow(id);
        return categoryMapper.toCategoryResponseDto(category);
    }

    @Override
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto) {
        Category category = findCategoryByIdOrThrow(id);
        String normalizedName = categoryRequestDto.getName().trim();

        if (! category.getName().equalsIgnoreCase(normalizedName) &&existsByName(normalizedName)){
            throw new RuntimeException("marka mevcut");
        }
        category.setName(normalizedName);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryResponseDto(savedCategory);
    }

    @Override
    public void deleteCategoryById(Long id) {
        Category category = findCategoryByIdOrThrow(id);
        categoryRepository.delete(category);
    }
}
