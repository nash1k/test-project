package com.example.config;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Configuration for the Kafka consumer.
 * All properties with prefix kafka.consumer will be passed to the kafka consumer
 * eg kafka.consumer.auto.offset.reset or kafka.consumer.group.id
 */
@ConfigurationProperties(prefix = "kafka")
@EnableConfigurationProperties
@Setter
public class KafkaConsumerConfig {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    private String consumerTopic;
    private Map<String, String> consumer = new HashMap<>();

    /**
     * @return the {@link Map} of {@link String} Kafka Consumer properties configured
     */
    public Map<String, String> getConsumer() {
        LOG.debug("Consumer configuration: {}", consumer);
        return consumer;
    }

    /**
     * @return the {@link String} Consumer Topic
     */
    public String getConsumerTopic() {
        LOG.debug("Consumer topic: {}", consumerTopic);
        return consumerTopic;
    }

    /**
     * @return the {@link List} of {@link String} topics configured
     */
    public List<String> getConsumerTopicsAsList() {
        return new ArrayList<>(Arrays.asList(getConsumerTopicsAsArray()));
    }

    /**
     * @return the {@link String} array of topics configured
     */
    public String[] getConsumerTopicsAsArray() {
        return Arrays.stream(getConsumerTopic().split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }
}
