package com.example.repository;

import com.example.entity.Candidate;
import com.example.entity.Vacancy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Simple {@link Repository} realisation for the {@link Candidate}s
 */
public interface CandidateRepository extends CrudRepository<Candidate, Long> {
}
