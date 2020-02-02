package com.example.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

/**
 * {@link Entity} class for manipulations by candidates' data
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name="candidate")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String email;

    @Column(name = "ctn", nullable = false)
    private String telephoneNumber;

    @Column(nullable = false)
    private String surname;

    @Column(name = "anonym", nullable = false)
    private boolean isAnonymMode;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    //original city_id without joined entity - use it if you don't want to get full info - just id
    @Column(name = "city_id", updatable = false, insertable = false)
    private Integer cityId;
}
