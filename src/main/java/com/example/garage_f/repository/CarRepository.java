package com.example.garage_f.repository;

import com.example.garage_f.model.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends CrudRepository<Car, Integer> {

    List<Car> findAllByOwnerID(int id);
}
