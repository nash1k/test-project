package com.example.service;

import com.example.config.KafkaConsumerConfig;
import com.example.domain.InputEvent;
import com.example.entity.Candidate;
import com.example.entity.Vacancy;
import com.google.gson.Gson;
import lombok.Getter;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link Service} for reading events from the topic
 */
@Service
public class ConsumerService {
    @Getter
    private static final Logger LOG = LoggerFactory.getLogger(ConsumerService.class);
    private static final Gson GSON = new Gson();

    private final Serde<String> stringSerdes = Serdes.String();

    @Autowired
    private KafkaConsumerConfig kafkaConsumerConfig;

    @Autowired
    private Transformer<String, InputEvent, KeyValue<Candidate, Set<String>>> repositoryService;

    @Autowired
    private Informer informerService;

    private final Properties consumerStreamsConfiguration = new Properties();
    private KafkaStreams streams;
    @Getter
    private volatile AtomicInteger invalidCount = new AtomicInteger();

    /**
     * Setup the Kafka Consumer
     *
     * @throws Exception if any error occurs during the setup of the consumer
     */
    @PostConstruct
    public void setup() throws Exception {
        LOG.debug("Setting up Trigger KafkaStreams");

        consumerStreamsConfiguration.putAll(kafkaConsumerConfig.getConsumer());
        consumerStreamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, stringSerdes.getClass());
        consumerStreamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, stringSerdes.getClass());

        consumeMessages();
    }

    /**
     * Consume messages from kafka topics and operate by the messages
     */
    private void consumeMessages() {
        LOG.info("Create streams for Reading messages from Consumer");

        final KStreamBuilder builder = new KStreamBuilder();
        final List<String> consumerTopics = kafkaConsumerConfig.getConsumerTopicsAsList();

        for (int topicIdx = 0; topicIdx < consumerTopics.size(); topicIdx++) {
            final String topic = consumerTopics.get(topicIdx);
            final KStream<String, String> messages = builder.stream(topic);
            messages
                    .flatMapValues(message -> {
                        try {
                            LOG.debug("Read message {} from topic:{}", message, topic);
                            return Collections.singletonList(GSON.fromJson(message, InputEvent.class));
                        } catch (final Exception e) {
                            LOG.error("Could not parse message. Topic: {}; message: {}; exception: {}", topic, message, e);
                            return Collections.emptyList();
                        }
                    })
                    .transform(() -> repositoryService)
                    .foreach((candidate, companyList) -> {
                        LOG.debug("Informing companies about great candidate: {}", candidate);
                        companyList.forEach(companyId -> {
                            final boolean isSuccess = informerService.inform(candidate, companyId);
                            if (isSuccess) {
                                LOG.debug("Company {} was successfully informed about candidate {}", companyId, candidate.getId());
                            } else {
                                invalidCount.getAndIncrement();
                            }
                        });
                    });
        }
        streams = new KafkaStreams(builder, consumerStreamsConfiguration);
        streams.start();
    }

    /**
     * Shutdown application
     */
    @PreDestroy
    public void shutdown() {
        LOG.debug("Closing SerDe");
        stringSerdes.close();
        streams.close(1, TimeUnit.SECONDS);
    }
}
