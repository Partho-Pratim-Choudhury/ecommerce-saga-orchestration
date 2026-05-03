package com.saga.order.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String ORDER_EVENTS_TOPIC = "order-events";

    public void publishOrderCreated(OrderCreatedEvent event) {
        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(ORDER_EVENTS_TOPIC, event.getOrderId(), event);

        future.whenComplete((result, error) -> {
            if(error == null) {
                log.info("Published OrderCreated event | orderId = {} | partition = {} | offset = {}",
                        event.getOrderId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Failed to publish OrderCreated event | orderId = {} | error = {}",
                        event.getOrderId(), error.getMessage());

            }
        });
    }
}
