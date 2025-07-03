package textprocessingapp.adapter.out;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import textprocessingapp.dto.ParagraphProcessDto;

@ExtendWith(MockitoExtension.class)
class KafkaProducerServiceTest {
  private KafkaProducerService kafkaProducerService;

  @Mock private KafkaTemplate<String, Object> kafkaTemplate;

  @BeforeEach
  void setUp() {
    kafkaProducerService = new KafkaProducerService(kafkaTemplate);
  }

  @Test
  void sendResult_sendsSerializedJson() throws Exception {
    // Arrange
    ParagraphProcessDto paragraphProcessDto = new ParagraphProcessDto("bar", 1.0, 3L);
    ArgumentCaptor<Message<String>> messageCaptor = ArgumentCaptor.forClass(Message.class);
    CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();
    when(kafkaTemplate.send(messageCaptor.capture())).thenReturn(future);

    setTopicField("text-processing-results");

    // Act
    kafkaProducerService.sendResult(paragraphProcessDto, "key1");

    // Assert
    Message<String> sentMessage = messageCaptor.getValue();
    assertMessageHeaders(sentMessage, "text-processing-results", "key1");
    assertPayloadContains(sentMessage, "bar");
    assertPayloadContains(sentMessage, "avgParagraphSize");
    assertPayloadContains(sentMessage, "totalProcessingTime");
  }

  @Test
  void sendResult_throwsOnSerializationError() {
    // Arrange
    ParagraphProcessDto paragraphProcessDto = mock(ParagraphProcessDto.class);
    when(paragraphProcessDto.getFreqWord()).thenThrow(new RuntimeException("fail"));

    // Act & Assert
    assertThrows(
        RuntimeException.class, () -> kafkaProducerService.sendResult(paragraphProcessDto, "key2"));
  }

  private void setTopicField(String topic) throws Exception {
    java.lang.reflect.Field topicField = KafkaProducerService.class.getDeclaredField("topic");
    topicField.setAccessible(true);
    topicField.set(kafkaProducerService, topic);
  }

  private void assertMessageHeaders(
      Message<String> message, String expectedTopic, String expectedKey) {
    assertEquals(expectedTopic, message.getHeaders().get(KafkaHeaders.TOPIC));
    assertEquals(expectedKey, message.getHeaders().get(KafkaHeaders.KEY));
  }

  private void assertPayloadContains(Message<String> message, String expectedContent) {
    assertInstanceOf(String.class, message.getPayload());
    assertTrue(message.getPayload().contains(expectedContent));
  }
}
