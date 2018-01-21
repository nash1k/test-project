package com.example.repository;

import com.example.entity.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Simple {@link Repository} realisation for the {@link City}
 */
public interface CityRepository extends CrudRepository<City, Integer> {
}
