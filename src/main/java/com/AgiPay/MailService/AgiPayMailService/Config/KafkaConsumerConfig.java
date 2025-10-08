package com.AgiPay.MailService.AgiPayMailService.Config;

import com.AgiPay.MailService.AgiPayMailService.DTO.TransactionNotificationDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;


@EnableKafka
@Configuration
public class KafkaConsumerConfig {


    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public ConsumerFactory<String, TransactionNotificationDTO> transactionConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "email-group");

        // chave como String
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // usar ErrorHandlingDeserializer delegando para JsonDeserializer via propriedades
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        // informar o tipo default do DTO e pacotes confiáveis via propriedades (não instanciar JsonDeserializer)
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, TransactionNotificationDTO.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.AgiPay.MailService.AgiPayMailService.DTO");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionNotificationDTO> transactionListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransactionNotificationDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transactionConsumerFactory());
        return factory;
     }
    }



