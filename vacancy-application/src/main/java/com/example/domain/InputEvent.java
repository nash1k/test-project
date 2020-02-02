package com.example.domain;

import lombok.*;

import java.io.Serializable;

/**
 * Domain class for input confirmed Candidate's interest.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class InputEvent implements Serializable {
    private static final long serialVersionUID = -2297477803276138370L;

    private Long candidateId;
    private Long vacancyId;
}
