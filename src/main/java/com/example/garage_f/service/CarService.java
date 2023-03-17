package com.example.garage_f.service;

import com.example.garage_f.model.Car;
import com.example.garage_f.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Cacheable(value = "ownersCars")
    public List<Car> findAllByOwnerID(int ownerID) {
        return carRepository.findAllByOwnerID(ownerID);
    }

    @Cacheable(value = "car", key = "#id")
    public Car findById(int id) {
        return carRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @Caching(evict = {
            @CacheEvict(value = "ownersCars", allEntries = true),
            @CacheEvict(value = "car", key = "#car.id")})
    public Car save(Car car) {
        return carRepository.save(car);
    }

    @Caching(evict = {
            @CacheEvict(value = "ownersCars", allEntries = true),
            @CacheEvict(value = "car", key = "#id")})
    public void deleteById(int id) {
        carRepository.deleteById(id);
    }
}
