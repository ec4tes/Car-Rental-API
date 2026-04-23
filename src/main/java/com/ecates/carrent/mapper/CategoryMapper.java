package com.ecates.carrent.mapper;


import com.ecates.carrent.dto.request.CategoryRequestDto;
import com.ecates.carrent.dto.response.CategoryResponseDto;
import com.ecates.carrent.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponseDto toCategoryResponseDto(Category category);

    Category toEntity (CategoryRequestDto categoryRequestDto);

    List<CategoryResponseDto> toCategoryResponseDtoList(List<Category> categories);

}
