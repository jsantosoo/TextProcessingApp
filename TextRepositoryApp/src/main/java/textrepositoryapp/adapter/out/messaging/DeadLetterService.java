package textrepositoryapp.adapter.out.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeadLetterService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String DEAD_LETTER_TOPIC = "words.processed.dlq";

    public void sendToDeadLetterTopic(ConsumerRecord<String, String> record, Exception e) {
        try {
            String errorMessage = String.format("Deadlettered record. Key: %s, Value: %s, Error: %s", record.key(), record.value(), e.getMessage());
            kafkaTemplate.send(DEAD_LETTER_TOPIC, record.key(), errorMessage);
            log.warn("Sent record to dead letter topic: {}", DEAD_LETTER_TOPIC);
        } catch (Exception ex) {
            log.error("Failed to send record to dead letter topic", ex);
        }
    }
}

