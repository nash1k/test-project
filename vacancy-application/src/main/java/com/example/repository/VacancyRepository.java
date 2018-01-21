package com.example.repository;

import com.example.entity.Vacancy;
import com.example.enums.VacancyState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link Repository} realisation for the {@link Vacancy}
 */
public interface VacancyRepository extends CrudRepository<Vacancy, Long> {
    /**
     * find all vacancies by the city, state and name
     * @return founded {@link List} of {@link Vacancy}
     */
    List<Vacancy> findAllByCityIdAndStateAndName(Integer cityId,
                                                 VacancyState state,
                                                 String name);
    /**
     * find all vacancies by the city
     * @return founded {@link List} of {@link Vacancy}
     */
    List<Vacancy> findAllByCityId(Integer cityId);
}
