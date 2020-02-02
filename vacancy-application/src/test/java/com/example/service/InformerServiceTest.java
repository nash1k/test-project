package com.example.service;

import com.example.entity.Candidate;
import com.example.util.EntityUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Test for {@link InformerService} class
 */
@TestPropertySource("classpath:application.properties")
@RunWith(SpringJUnit4ClassRunner.class)
@RestClientTest(InformerService.class)
public class InformerServiceTest {

    @Autowired
    InformerService informerService;

    @Autowired
    private MockRestServiceServer server;

    private static final String COMPANY = "123";
    private static final Candidate CANDIDATE = EntityUtil.createCandidate(1L, "Sergey", "Petrov", null, true);

    @Test
    public void successWithCorrectAnswerTest() {

        this.server.expect(requestTo(COMPANY))
                .andRespond(withSuccess("accepted", MediaType.APPLICATION_JSON));
        final boolean result = informerService.inform(CANDIDATE, COMPANY);
        Assert.assertTrue(result);
    }

    @Test
    public void unsuccessfulResultTest() {
        this.server.expect(requestTo(COMPANY))
                .andRespond(withStatus(HttpStatus.FORBIDDEN));
        final boolean result = informerService.inform(CANDIDATE, COMPANY);
        Assert.assertFalse(result);
    }
}
