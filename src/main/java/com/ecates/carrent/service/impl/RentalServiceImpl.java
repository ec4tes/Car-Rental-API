package com.ecates.carrent.service.impl;

import com.ecates.carrent.dto.request.RentalRequestDto;
import com.ecates.carrent.dto.response.RentalResponseDto;
import com.ecates.carrent.entity.Car;
import com.ecates.carrent.entity.Customer;
import com.ecates.carrent.entity.Rental;
import com.ecates.carrent.enums.RentalStatus;
import com.ecates.carrent.mapper.RentalMapper;
import com.ecates.carrent.repository.CarRepository;
import com.ecates.carrent.repository.CustomerRepository;
import com.ecates.carrent.repository.RentalRepository;
import com.ecates.carrent.service.RentalService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final CustomerRepository customerRepository;
    private final CarRepository carRepository;

    public RentalServiceImpl(RentalRepository rentalRepository, RentalMapper rentalMapper, CustomerRepository customerRepository, CarRepository carRepository) {
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;
        this.customerRepository = customerRepository;
        this.carRepository = carRepository;
    }

    private Rental findRentalByIdOrThrow(Long id){
        return rentalRepository.findById(id).orElseThrow(()->new RuntimeException("Aradiginiz Id de bir Kiralma islemi bulunamadi"));
    }

    private Customer findCustomerByIdOrThrow(Long id){
        return customerRepository.findById(id).orElseThrow(()-> new RuntimeException("Aradiginiz Id de Bir Musteri Bulunamadi"));
    }

    private Car findCarByIdOrThrow(Long id){
        return carRepository.findById(id).orElseThrow(()->new RuntimeException("Aradiginiz id de araba bulunamadi"));
    }

    private void validateRentalDate(LocalDate startDate, LocalDate endDate){
        if (!endDate.isAfter(startDate)){
            throw new RuntimeException("Bitis tarihi baslangictan once olmalidir");
        }
    }

    private long calculateRentalDays(LocalDate startDate, LocalDate endDate){
        return ChronoUnit.DAYS.between(startDate,endDate);
    }

    private BigDecimal calculateTotalPrice(BigDecimal dailyPrice,long totalDays){
        return dailyPrice.multiply(BigDecimal.valueOf(totalDays));
    }

    private void validateCarAvailability(Long carId, LocalDate startDate, LocalDate endDate){
        if (rentalRepository.existsByCarIdAndStartDateLessThanAndEndDateGreaterThan(carId,startDate,endDate)){
            throw new RuntimeException("Arac belirtilen tarihlerde reserve edilmis baska tarih secmelisiniz");
        }
    }



    @Override
    public RentalResponseDto createRental(RentalRequestDto rentalRequestDto) {
        LocalDate startDate = rentalRequestDto.getStartDate();
        LocalDate endDate = rentalRequestDto.getEndDate();

        validateRentalDate(startDate,endDate);

        Car car = findCarByIdOrThrow(rentalRequestDto.getCarId());
        validateCarAvailability(car.getId(),startDate,endDate);

        Customer customer = findCustomerByIdOrThrow(rentalRequestDto.getCustomerId());

        long days = calculateRentalDays(startDate,endDate);
        BigDecimal totalPrice = calculateTotalPrice(car.getDailyPrice(), days);

        Rental rental = Rental.builder()
                .customer(customer)
                .car(car)
                .startDate(startDate)
                .endDate(endDate)
                .totalPrice(totalPrice)
                .status(RentalStatus.RESERVED)
                .build();

        Rental savedRental = rentalRepository.save(rental);

        return rentalMapper.toRentalResponseDto(savedRental);

    }

    @Override
    public List<RentalResponseDto> getAllRental() {
        List<Rental> rentalRepositoryAll = rentalRepository.findAll();
        return rentalMapper.toRentalResponseDtoList(rentalRepositoryAll);
    }

    @Override
    public RentalResponseDto getRentalById(Long id) {
        Rental rental = findRentalByIdOrThrow(id);
        return rentalMapper.toRentalResponseDto(rental);
    }

    @Override
    public RentalResponseDto completeRental(Long id) {
        Rental rental = findRentalByIdOrThrow(id);
        LocalDate now = LocalDate.now();

        rental.setActualReturnDate(now);

        if (now.isAfter(rental.getEndDate())){
            rental.setStatus(RentalStatus.LATE);
        }
        else {
            rental.setStatus(RentalStatus.COMPLETED);
        }
        Rental savedRental = rentalRepository.save(rental);
        return rentalMapper.toRentalResponseDto(savedRental);
    }

    @Override
    public RentalResponseDto cancelRental(Long id) {
        Rental rental = findRentalByIdOrThrow(id);
        rental.setStatus(RentalStatus.CANCELLED);
        Rental savedRenatal = rentalRepository.save(rental);
        return rentalMapper.toRentalResponseDto(savedRenatal);
    }
}
