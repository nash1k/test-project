package com.example.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * {@link Entity} class for manipulations by cities' data (just for 3NF)
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name="city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
}
