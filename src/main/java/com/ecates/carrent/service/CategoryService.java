package com.ecates.carrent.service;

import com.ecates.carrent.dto.request.CategoryRequestDto;
import com.ecates.carrent.dto.response.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);

    List<CategoryResponseDto> getAllCategory();

    CategoryResponseDto getByIdCategory(Long id);

    CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto);

    void deleteCategoryById(Long id);

}
