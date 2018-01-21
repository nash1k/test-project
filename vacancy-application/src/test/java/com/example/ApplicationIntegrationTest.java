package com.example;

import com.example.config.KafkaConsumerConfig;
import com.example.domain.InputEvent;
import com.example.entity.Candidate;
import com.example.repository.CandidateRepository;
import com.example.service.ConsumerService;
import com.example.service.InformerService;
import com.google.gson.Gson;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@DataJpaTest
@ContextConfiguration(classes = { KafkaConsumerConfig.class })
@TestPropertySource({ "classpath:application.properties" })
@EntityScan("com.example")
@ComponentScan
@RestClientTest(InformerService.class)
@RunWith(SpringRunner.class)
public class ApplicationIntegrationTest {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationIntegrationTest.class);

    private static final Gson GSON = new Gson();
    private static final String TOPIC = "test_topic";
    private static final StringSerializer stringSerializer = new StringSerializer();
    private static KafkaProducer<String, String> kafkaProducer;

    @SpyBean
    private InformerService informerServiceSpy;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private CandidateRepository candidateRepository;

    /**
     * Create instance of embedded kafka for tests
     */
    @ClassRule
    public static KafkaEmbedded embeddedKafka =
            new KafkaEmbedded(1, true, 2, TOPIC);

    /**
     * Initialization of embedded kafka. Get hosts of bootstrap server.
     * Also set listener for kafka
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        LOG.debug("Configuring Kafka Producer");
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafka.getBrokersAsString());
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        kafkaProducer = new KafkaProducer<>(props, stringSerializer, stringSerializer);

        //Need it for KafkaConsumerConfig
        System.setProperty("kafka.consumer.bootstrap.servers", embeddedKafka.getBrokersAsString());
    }

    @Test
    public void doNotInformByDifferentConditionsTest() throws Exception {
        //Given
        final List<InputEvent> events = new ArrayList<>();
        //Filtered by anonymMode
        events.add(new InputEvent(4L, 6L));
        //No another vacancies in the city at all
        events.add(new InputEvent(1L, 11L));
        //No vacancies with current name
        events.add(new InputEvent(2L, 8L));
        //No active vacancies
        events.add(new InputEvent(3L, 12L));
        //When
        sendMessageToKafka(events);
        //Should give some time for the kafka streams and logic process
        Thread.sleep(1000);
        //Then
        verify(informerServiceSpy, never()).inform(any(Candidate.class), anyString());
    }

    @Test
    public void successInformTest() throws Exception {
        //Given
        int invalidCountBefore = consumerService.getInvalidCount().get();
        this.server.expect(requestTo("10"))
                .andRespond(withSuccess("accepted", MediaType.APPLICATION_JSON));
        //When
        sendMessageToKafka(Collections.singletonList(new InputEvent(1L, 1L)));
        //Should give some time for the kafka streams and logic process
        Thread.sleep(2000);
        //Then
        final Candidate candidate = candidateRepository.findOne(1L);
        //inform was called for all results
        verify(informerServiceSpy, times(1)).inform(any(Candidate.class), anyString());
        verify(informerServiceSpy, times(1)).inform(candidate, "10");
        assertEquals("Should not be any invalid sending attempts", consumerService.getInvalidCount().get() - invalidCountBefore, 0);
    }

    @Test
    public void unsuccessInformTest() throws Exception {
        //Given
        int invalidCountBefore = consumerService.getInvalidCount().get();
        this.server.expect(requestTo("OK"))
                .andRespond(withStatus(HttpStatus.FORBIDDEN));
        //When
        sendMessageToKafka(Collections.singletonList(new InputEvent(2L, 10L)));
        //Should give some time for the kafka streams and logic process
        Thread.sleep(2000);
        //Then
        final Candidate candidate = candidateRepository.findOne(2L);
        //inform was called for all results
        verify(informerServiceSpy, times(1)).inform(any(Candidate.class), anyString());
        verify(informerServiceSpy, times(1)).inform(candidate, "OK");
        assertEquals("Should be one invalid sending attempt", 1, consumerService.getInvalidCount().get() - invalidCountBefore);
    }

    private void sendMessageToKafka(final List<InputEvent> events) throws Exception {
        for (InputEvent event : events) {
            final Future<RecordMetadata> metadataFuture =
                    kafkaProducer.send(new ProducerRecord<>(TOPIC, "1", GSON.toJson(event)));
            metadataFuture.get();
        }
    }


    @AfterClass
    public static void shutdown() {
        kafkaProducer.close();
        stringSerializer.close();
    }
}
