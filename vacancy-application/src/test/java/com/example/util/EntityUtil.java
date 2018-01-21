package com.example.util;

import com.example.entity.Candidate;
import com.example.entity.City;
import com.example.entity.Vacancy;
import com.example.enums.VacancyState;

public class EntityUtil {
    public static City createCity(Integer id, String name) {
        final City city = new City();
        city.setId(id);
        city.setName(name);
        return city;
    }

    public static Candidate createCandidate(Long id, String firstName, String surname,
                                            City city, boolean anonymMode) {
        Candidate candidate = new Candidate();
        candidate.setId(id);
        candidate.setFirstName(firstName);
        candidate.setSurname(surname);
        candidate.setAnonymMode(anonymMode);
        candidate.setCity(city);
        candidate.setEmail("common@gmail.com");
        candidate.setTelephoneNumber("95111122233");

        return candidate;
    }

    public static Vacancy createVacancy(Long id, String name,
                                        City city, String companyId, VacancyState state) {
        Vacancy vacancy = new Vacancy();
        vacancy.setId(id);
        vacancy.setCity(city);
        vacancy.setCompanyId(companyId);
        vacancy.setDescription("Great position in the great company!");
        vacancy.setName(name);
        vacancy.setSalary(Double.valueOf(190000));
        vacancy.setState(state);

        return vacancy;
    }
}
