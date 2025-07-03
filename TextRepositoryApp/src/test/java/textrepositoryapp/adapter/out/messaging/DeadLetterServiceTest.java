package textrepositoryapp.adapter.out.messaging;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeadLetterServiceTest {
    private KafkaTemplate<String, String> kafkaTemplate;
    private DeadLetterService deadLetterService;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        deadLetterService = new DeadLetterService(kafkaTemplate);
    }

    @Test
    void sendToDeadLetterTopic_sendsMessageToDLQ() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic", 0, 0L, "key1", "value1");
        Exception exception = new RuntimeException("Test error");

        deadLetterService.sendToDeadLetterTopic(record, exception);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplate).send(eq("words.processed.dlq"), keyCaptor.capture(), valueCaptor.capture());
        assertEquals("key1", keyCaptor.getValue());
        assertTrue(valueCaptor.getValue().contains("Deadlettered record. Key: key1, Value: value1, Error: Test error"));
    }

    @Test
    void sendToDeadLetterTopic_handlesKafkaTemplateException() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic", 0, 0L, "key2", "value2");
        Exception exception = new RuntimeException("Another error");
        doThrow(new RuntimeException("Kafka send failed")).when(kafkaTemplate).send(anyString(), anyString(), anyString());

        assertDoesNotThrow(() -> deadLetterService.sendToDeadLetterTopic(record, exception));
    }
}

