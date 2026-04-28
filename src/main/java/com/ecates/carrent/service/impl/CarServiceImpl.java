package com.ecates.carrent.service.impl;

import com.ecates.carrent.dto.request.CarRequestDto;
import com.ecates.carrent.dto.response.CarResponseDto;
import com.ecates.carrent.entity.Brand;
import com.ecates.carrent.entity.Car;
import com.ecates.carrent.entity.Category;
import com.ecates.carrent.mapper.CarMapper;
import com.ecates.carrent.repository.BrandRepository;
import com.ecates.carrent.repository.CarRepository;
import com.ecates.carrent.repository.CategoryRepository;
import com.ecates.carrent.service.CarService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public CarServiceImpl(CarRepository carRepository, CarMapper carMapper, BrandRepository brandRepository, CategoryRepository categoryRepository) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    private Car findCarByIdOrThrow(Long id){
        return carRepository.findById(id).orElseThrow(() -> new RuntimeException("araba bulunamadi"));
    }

    private Brand findBrandByIdOrThrow(Long id){
        return brandRepository.findById(id).orElseThrow(() -> new RuntimeException("Marka Bulnamadi"));
    }

    private Category findCategoryByIdOrThrow(Long id){
        return categoryRepository.findById(id).orElseThrow(()->new RuntimeException("kategori bulunamadi"));
    }

    private boolean existsByPlateNumber(String plateNumber){
        return carRepository.existsByPlateNumberIgnoreCase(plateNumber);
    }

    private String normalizeText (String value){
        return value.trim();
    }

    private String normalizePlateNumber(String plateNumber){
        return plateNumber.trim().toUpperCase();
    }

    private void validatePlateNumberForCreate(String plateNumber) {
        if (existsByPlateNumber(plateNumber)) {
            throw new RuntimeException("Bu plakaya sahip araç zaten mevcut.");
        }
    }

    private void applyCarFields(Car car,
                                String model,
                                String plateNumber,
                                Integer year,
                                BigDecimal dailyPrice,
                                Brand brand,
                                Category category) {

        car.setModel(model);
        car.setPlateNumber(plateNumber);
        car.setYear(year);
        car.setDailyPrice(dailyPrice);
        car.setBrand(brand);
        car.setCategory(category);
    }

    private void validatePlateNumberForUpdate(Car car,String plateNumber){
        if (!car.getPlateNumber().equalsIgnoreCase(plateNumber)&&existsByPlateNumber(plateNumber)){
            throw new RuntimeException("bu plakada araba var");
        }

    }


    @Override
    public CarResponseDto createCar(CarRequestDto carRequestDto) {
        String model = normalizeText(carRequestDto.getModel());
        String plateNumber = normalizePlateNumber(carRequestDto.getPlateNumber());

        validatePlateNumberForCreate(plateNumber);

        Brand brand = findBrandByIdOrThrow(carRequestDto.getBrandId());
        Category category = findCategoryByIdOrThrow(carRequestDto.getCategoryId());

        Car car = carMapper.toEntity(carRequestDto);

        applyCarFields(
                car,
                model,
                plateNumber,
                carRequestDto.getYear(),
                carRequestDto.getDailyPrice(),
                brand,
                category
        );

        Car savedCar = carRepository.save(car);

        return carMapper.toCarResponseDto(savedCar);
    }

    @Override
    public List<CarResponseDto> getAllCars() {
        List<Car> carRepositoryAll = carRepository.findAll();
        return carMapper.toCarResposeDtoList(carRepositoryAll);
    }

    @Override
    public CarResponseDto getCarById(Long id) {
        Car car = findCarByIdOrThrow(id);
        return carMapper.toCarResponseDto(car);
    }



    @Override
    public CarResponseDto updateCar(Long id, CarRequestDto carRequestDto) {
        Car car = findCarByIdOrThrow(id);
        String model = normalizeText(carRequestDto.getModel());
        String plateNumber = normalizePlateNumber(carRequestDto.getPlateNumber());

        Brand brand = findBrandByIdOrThrow(carRequestDto.getBrandId());
        Category category = findCategoryByIdOrThrow(carRequestDto.getCategoryId());

        validatePlateNumberForUpdate(car,plateNumber);

        applyCarFields(
                car,
                model,
                plateNumber,
                carRequestDto.getYear(),
                carRequestDto.getDailyPrice(),
                brand,
                category
        );

        Car updatedCar = carRepository.save(car);
        return carMapper.toCarResponseDto(updatedCar);

    }

    @Override
    public void deleteCar(Long id) {
        Car car = findCarByIdOrThrow(id);
        carRepository.delete(car);
    }
}
