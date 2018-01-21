package com.example.service;

import com.example.entity.Candidate;
import com.example.entity.Vacancy;
import org.springframework.http.HttpStatus;

import java.util.List;

@FunctionalInterface
public interface Informer<T extends Candidate, String> {
    boolean inform(T candidate, String companyId);
}
