package com.example.service;

import com.example.entity.Candidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * {@link Service} for informing companies about candidate for their vacancies via REST HTTP
 */
@Service
public class InformerService implements Informer<Candidate, String> {
    private static final Logger LOG = LoggerFactory.getLogger(InformerService.class);

    @Value("${company.url}")
    private String companyUrl;

    @Value("${basic.auth.username:username}")
    private String basicAuthUsername;

    @Value("${basic.auth.password:password}")
    private String basicAuthPassword;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private RestTemplate restTemplate;

    /**
     * Configuring the {@link RestTemplate}
     */
    @PostConstruct
    public void init() {
        LOG.debug("Configuring RestTemplate for sending http requests to remote service");
        restTemplate = restTemplateBuilder
                .rootUri(companyUrl)
                .basicAuthorization(basicAuthUsername, basicAuthPassword)
                .build();
    }

    /**
     * Informing companies about interesting candidate
     * @param candidate is current {@link Candidate}
     * @param companyId for informing
     *
     * @return true - if informing successfully completed, else - otherwise
     */
    @Override
    public boolean inform(final Candidate candidate, final String companyId) {
        LOG.debug("Informing company {} about great candidate: {}", companyId, candidate);
        try {
            final ResponseEntity<String> exchange = restTemplate.exchange("/{company}", HttpMethod.POST,
                    new HttpEntity<>(candidate), String.class, companyId);
            return exchange.getStatusCode().equals(HttpStatus.OK);
        } catch (final HttpClientErrorException hcee) {
            LOG.error("Error by informing company {} about candidate {}", companyId, candidate, hcee);
            return false;
        }
    }
}
