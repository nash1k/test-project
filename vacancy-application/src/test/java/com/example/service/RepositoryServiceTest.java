package com.example.service;

import com.example.domain.InputEvent;
import com.example.entity.Candidate;
import com.example.entity.City;
import com.example.entity.Vacancy;
import com.example.enums.VacancyState;
import com.example.repository.CandidateRepository;
import com.example.repository.VacancyRepository;
import com.example.util.EntityUtil;
import org.apache.kafka.streams.KeyValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {RepositoryService.class})
@RunWith(SpringRunner.class)
public class RepositoryServiceTest {
    @Autowired
    private RepositoryService repositoryService;

    @MockBean
    private CandidateRepository candidateRepository;

    @MockBean
    private VacancyRepository vacancyRepository;

    @Test
    public void doNothingWithAnonymTest() {
        //Given
        final City city = EntityUtil.createCity(1, "Milan");
        final Candidate candidate = EntityUtil.createCandidate(1L, "Sergey", "Petrov", city, true);
        when(candidateRepository.findOne(anyLong())).thenReturn(candidate);
        //When
        KeyValue<Candidate, Set<String>> result = repositoryService.transform("111", new InputEvent(1L, 123L));
        //Then
        assertNull("Should be empty result", result);
    }

    @Test
    public void serviceLogicTest() {
        //Given
        final City city = EntityUtil.createCity(2, "Moscow");
        final Candidate candidate = EntityUtil.createCandidate(1L, "Sergey", "Petrov", city, false);
        final Vacancy originalVacancy = EntityUtil.createVacancy(123L, "java developer", city, "AAA", VacancyState.ACTIVE);
        final List<Vacancy> cityVacancies = new ArrayList<>();
        cityVacancies.add(EntityUtil.createVacancy(130L, "java developer", city, "AAA", VacancyState.ACTIVE));
        cityVacancies.add(EntityUtil.createVacancy(140L, "java developer", city, "BBB", VacancyState.ACTIVE));
        cityVacancies.add(EntityUtil.createVacancy(150L, "java developer", city, "BBB", VacancyState.ACTIVE));
        cityVacancies.add(EntityUtil.createVacancy(160L, "java developer", city, "CCC", VacancyState.ACTIVE));
        when(candidateRepository.findOne(anyLong())).thenReturn(candidate);
        when(vacancyRepository.findOne(anyLong())).thenReturn(originalVacancy);
        when(vacancyRepository.findAllByCityIdAndStateAndName(2, VacancyState.ACTIVE, "java developer")).thenReturn(cityVacancies);

        //When
        KeyValue<Candidate, Set<String>> result = repositoryService.transform("111", new InputEvent(1L, 123L));
        //Then
        assertFalse("Result should not contain original company id", result.value.contains(originalVacancy.getCompanyId()));
        assertEquals("Incorrect count of results", 2, result.value.size());
    }
}
