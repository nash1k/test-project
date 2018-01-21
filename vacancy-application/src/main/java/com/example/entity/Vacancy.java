package com.example.entity;

import com.example.enums.VacancyState;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * {@link Entity} class for manipulations by vacancies' data
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name="vacancy")
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "company_id")
    private String companyId;
    private String name;
    private String description;
    private Double salary;
    @Enumerated(value = EnumType.STRING)
    private VacancyState state;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
}
