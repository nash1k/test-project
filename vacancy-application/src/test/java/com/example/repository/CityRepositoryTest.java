package com.example.repository;

import com.example.entity.City;
import com.example.util.EntityUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DataJpaTest
@RunWith(SpringRunner.class)
public class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    private City city;

    @Before
    public void setup() {
        this.city = EntityUtil.createCity(2, "Voronezh");
    }

    @Test
    public void findByPrimaryKeyTest() {
        //When
        City foundCity = cityRepository.findOne(city.getId());
        //Then
        assertNotNull(foundCity);
        assertEquals("City result is incorrect", city, foundCity);
    }
}