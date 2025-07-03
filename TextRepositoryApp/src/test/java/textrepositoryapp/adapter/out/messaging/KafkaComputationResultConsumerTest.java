package textrepositoryapp.adapter.out.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import textrepositoryapp.adapter.out.mapper.ComputationResultMapper;
import textrepositoryapp.adapter.out.persistence.ComputationResultRepository;
import textrepositoryapp.domain.ComputationResult;
import textrepositoryapp.domain.ComputationResultDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaComputationResultConsumerTest {

    @Mock
    private ComputationResultRepository repository;

    @Mock
    private ComputationResultMapper computationResultMapper;

    @Mock
    private DeadLetterService deadLetterService;
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private KafkaComputationResultConsumer consumer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        consumer = new KafkaComputationResultConsumer(repository, computationResultMapper, deadLetterService);
    }

    @Test
    void listen_shouldSaveResult_whenRecordIsValid() throws Exception {
        // Arrange
        ComputationResultDTO dto = ComputationResultDTO.builder()
            .freqWord("test")
            .avgParagraphSize(5.0)
            .totalProcessingTime(100.0)
            .build();
        String value = objectMapper.writeValueAsString(dto);
        ConsumerRecord<String, String> record = new ConsumerRecord<>("words.processed", 0, 0L, "key", value);
        ComputationResult mockResult = mock(ComputationResult.class);
        when(computationResultMapper.toEntity(any(ComputationResultDTO.class), any())).thenReturn(mockResult);

        // Act
        consumer.listen(record);

        // Wait for async processing to complete
        Thread.sleep(500); // Adjust time if needed for your environment

        // Assert
        verify(repository, times(1)).save(any(ComputationResult.class));
    }

    @Test
    void listen_shouldDoNothing_whenRecordIsEmpty() {
        // Arrange
        ConsumerRecord<String, String> record = new ConsumerRecord<>("words.processed", 0, 0L, "key", "");

        // Act
        consumer.listen(record);

        // Assert
        verify(repository, never()).save(any());
    }

    @Test
    void listen_shouldNotThrowException_whenJsonIsInvalid() {
        // Arrange
        ConsumerRecord<String, String> record = new ConsumerRecord<>("words.processed", 0, 0L, "key", "not-json");

        // Act & Assert
        assertDoesNotThrow(() -> consumer.listen(record),
            "Expected no exception to be thrown when processing invalid JSON");
    }
}