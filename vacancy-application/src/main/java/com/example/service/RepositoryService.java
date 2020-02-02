package com.example.service;

import com.example.domain.InputEvent;
import com.example.entity.Candidate;
import com.example.entity.Vacancy;
import com.example.enums.VacancyState;
import com.example.repository.CandidateRepository;
import com.example.repository.VacancyRepository;
import lombok.AllArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Transform messages to result view using jpa repositories
 */
@Service
@AllArgsConstructor
public class RepositoryService implements Transformer<String, InputEvent, KeyValue<Candidate, Set<String>>> {
    private static final Logger LOG = LoggerFactory.getLogger(RepositoryService.class);

    private CandidateRepository candidateRepository;
    private VacancyRepository vacancyRepository;

    @Override
    public void init(ProcessorContext context) {}

    @Override
    public KeyValue<Candidate, Set<String>> transform(final String key, final InputEvent inputEvent) {
        LOG.debug("Searching companies for current candidate: {}", inputEvent.getCandidateId());
        var candidate = candidateRepository.findById(inputEvent.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Unknown candidate with candidateId=" + inputEvent.getCandidateId()));

        if (!candidate.isAnonymMode()) {
            //This place is one of the candidates for refactoring
            var originalVacancy = vacancyRepository.findById(inputEvent.getVacancyId())
                    .orElseThrow(() -> new RuntimeException("Unknown vacancy with vacancyId=" + inputEvent.getVacancyId()));
            LOG.debug("Searching vacancies with name: {}", originalVacancy.getName());
            var cityVacancies = vacancyRepository.findAllByCityIdAndStateAndName(
                    originalVacancy.getCity().getId(), VacancyState.ACTIVE, originalVacancy.getName());

            LOG.debug("Getting company's id from each vacancy");
            //collect to Set to have no duplicates
            var companies = cityVacancies.stream().map(Vacancy::getCompanyId).collect(Collectors.toSet());
            LOG.debug("Removing the company id of original vacancy {}", originalVacancy.getCompanyId());
            companies.removeIf(company -> company.equals(originalVacancy.getCompanyId()));
            if (companies.isEmpty()) {
                LOG.info("No companies for candidate by interests: {}", candidate);
                return null;
            }
            return KeyValue.pair(candidate, companies);
        } else {
            return null;
        }
    }

    @Override
    public void close() {}
}
