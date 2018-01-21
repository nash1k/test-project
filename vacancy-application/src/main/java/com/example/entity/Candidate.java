package com.example.entity;

import lombok.*;

import javax.persistence.*;

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
}
