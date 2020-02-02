package com.example.repository;

import com.example.entity.City;
import com.example.entity.Vacancy;
import com.example.enums.VacancyState;
import com.example.util.EntityUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DataJpaTest
@RunWith(SpringRunner.class)
public class VacancyRepositoryTest {

    @Autowired
    private VacancyRepository vacancyRepository;

    private Vacancy vacancy;
    private City city;

    @Before
    public void setup() {
        this.city = EntityUtil.createCity(3, "Milan");
        this.vacancy = EntityUtil.createVacancy(6L, "java developer", city, "10", VacancyState.ACTIVE);
    }

    @Test
    public void findByPrimaryKeyTest() {
        //When
        Vacancy foundVacancy = vacancyRepository.findById(vacancy.getId()).get();
        //Then
        assertNotNull(foundVacancy);
        assertEquals("Vacancy result is incorrect", vacancy, foundVacancy);
    }

    @Test
    public void noFindByNameAllByCityIdAndStateAndNameTest() {
        //When
        List<Vacancy> foundVacancies = vacancyRepository.findAllByCityIdAndStateAndName(city.getId(), VacancyState.ACTIVE, "c# developer");
//        foundVacancies.addAll(vacancyRepository.findAllByCityIdAndStateAndName(city.getId(), VacancyState.HOLD, "java developer"));
        foundVacancies.addAll(vacancyRepository.findAllByCityIdAndStateAndName(200, VacancyState.ACTIVE, "java developer"));
        foundVacancies.addAll(vacancyRepository.findAllByCityIdAndStateAndName(city.getId(), VacancyState.ACTIVE, "Java Developer"));

        //Then
        assertNotNull(foundVacancies);
        assertEquals("Incorrect size of result list", 0, foundVacancies.size());
    }

    @Test
    public void findByNameAllByCityIdAndStateAndNameTest() {
        //When
        List<Vacancy> foundVacancies = vacancyRepository.findAllByCityIdAndStateAndName(city.getId(), VacancyState.ACTIVE, "java developer");

        //Then
        assertNotNull(foundVacancies);
        assertEquals("Incorrect size of result list", 3, foundVacancies.size());
        assertEquals("Vacancy result is incorrect", vacancy, foundVacancies.get(1));
    }
}