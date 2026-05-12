package com.ecates.carrent.controller;

import com.ecates.carrent.dto.request.CategoryRequestDto;
import com.ecates.carrent.dto.response.CategoryResponseDto;
import com.ecates.carrent.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto){
        CategoryResponseDto category = categoryService.createCategory(categoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategory(){
        List<CategoryResponseDto> allCategory = categoryService.getAllCategory();
        return ResponseEntity.ok(allCategory);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getByIdCategory(@PathVariable Long id){
        CategoryResponseDto category = categoryService.getByIdCategory(id);
        return ResponseEntity.ok(category);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long id,@Valid @RequestBody CategoryRequestDto categoryRequestDto){
        CategoryResponseDto categoryResponseDto = categoryService.updateCategory(id, categoryRequestDto);
        return ResponseEntity.ok(categoryResponseDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCategory(@PathVariable Long id){
        categoryService.deleteCategoryById(id);
    }

}
