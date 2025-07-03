package textrepositoryapp.adapter.out.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.Instant;

import lombok.extern.slf4j.Slf4j;
import textrepositoryapp.adapter.out.mapper.ComputationResultMapper;
import textrepositoryapp.adapter.out.persistence.ComputationResultRepository;
import textrepositoryapp.domain.ComputationResult;
import textrepositoryapp.domain.ComputationResultDTO;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaComputationResultConsumer {

    private final ComputationResultRepository repository;
    private final ComputationResultMapper computationResultMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // Adjust pool size as needed
    private final DeadLetterService deadLetterService;

    @KafkaListener(
        topics = "words.processed",
        groupId = "repository-app-group",
        concurrency = "${kafka.consumer.concurrency:1}"
    )
    public void listen(ConsumerRecord<String, String> record) {
        log.info("Received record: key={}, value={}", record.key(), record.value());

        if (isRecordValueEmpty(record)) {
            log.info("Received empty or null record, skipping.");
            return;
        }

        executorService.submit(() -> {
            try {
                processRecordWithRetry(record);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Retryable(
        value = { IOException.class, RuntimeException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    private void processRecordWithRetry(ConsumerRecord<String, String> record) throws JsonProcessingException {
        ComputationResultDTO dto = objectMapper.readValue(record.value(), ComputationResultDTO.class);
        ComputationResult result = computationResultMapper.toEntity(dto, Instant.now());
        repository.save(result);
        log.info("Saved ComputationResult: {}", result);
    }

    @Recover
    private void recover(Exception e, ConsumerRecord<String, String> record) {
        log.error("Max retry attempts reached for record: {}. Sending to dead letter topic.", record.value(), e);
        deadLetterService.sendToDeadLetterTopic(record, e);
    }

    private boolean isRecordValueEmpty(ConsumerRecord<String, String> record) {
        return record.value() == null || record.value().isEmpty();
    }
}