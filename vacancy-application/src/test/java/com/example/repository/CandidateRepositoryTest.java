package com.example.repository;

import com.example.entity.Candidate;
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
public class CandidateRepositoryTest {

    @Autowired
    private CandidateRepository candidateRepository;

    private City city;
    private Candidate candidate;

    @Before
    public void setup() {
        this.city = EntityUtil.createCity(3, "Milan");
        this.candidate = EntityUtil.createCandidate(1L,"Sergey", "Sergeev", city, false);
    }

    @Test
    public void findByPrimaryKeyTest() {
        //When
        Candidate foundCandidate = candidateRepository.findOne(candidate.getId());
        //Then
        assertNotNull(foundCandidate);
        assertEquals("Candidate result is incorrect", candidate, foundCandidate);
    }

    @Test
    public void findAllTest() {
        //When
        Iterable<Candidate> foundCandidates = candidateRepository.findAll();
        //Then
        assertNotNull(foundCandidates);
        int count = 0;
        for (Candidate foundCandidate : foundCandidates) {
            assertEquals("Email is incorrect", "common@gmail.com", foundCandidate.getEmail());
            count++;
        }
        assertEquals("Count of results is incorrect", 4, count);
    }
}