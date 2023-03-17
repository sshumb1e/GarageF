package com.example.garage_f.service;

import com.example.garage_f.model.Car;
import com.example.garage_f.repository.CarRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;
    private CarService carService;

    @BeforeEach
    public void init() {
        carService = new CarService(carRepository);
    }

    @Test
    public void getAllByOwnerId() {

        List<Car> expectedCars = List.of(
                Car.builder().model("Car 1").build(),
                Car.builder().model("Car 2").build(),
                Car.builder().model("Car 3").build()
        );
        Mockito.when(carRepository.findAllByOwnerID(anyInt())).thenReturn(expectedCars);
        List<Car> cars = carService.findAllByOwnerID(anyInt());
        Assertions.assertEquals(cars, expectedCars);
    }

    @Test
    public void findByIdTest() {

        Car expectedCar = Car.builder().model("Test car").build();
        Mockito.when(carRepository.findById(anyInt())).thenReturn(Optional.of(expectedCar));
        Car car = carService.findById(anyInt());
        Assertions.assertEquals(car, expectedCar);
    }

    @Test
    public void findByIdNotFoundTest(){

        Mockito.when(carRepository.findById(anyInt())).thenReturn(Optional.empty());
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class, () -> carService.findById(anyInt()));
        Assertions.assertEquals(NOT_FOUND, responseStatusException.getStatusCode());
    }

    @Test
    public void saveTest() {

        Car testCar = Car.builder().model("Test car").build();
        Mockito.when(carRepository.save(any())).thenReturn(testCar);
        Car expectedCar = carService.save(Car.builder().build());
        Assertions.assertEquals(expectedCar, testCar);
    }

    @Test
    public void deleteTest() {

        doNothing().when(carRepository).deleteById(anyInt());
        carService.deleteById(anyInt());
        verify(carRepository, times(1)).deleteById(anyInt());
    }
}
