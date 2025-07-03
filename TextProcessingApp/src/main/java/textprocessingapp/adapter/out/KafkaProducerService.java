package textprocessingapp.adapter.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import textprocessingapp.dto.ParagraphProcessDto;
import textprocessingapp.port.ResultSenderPort;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService implements ResultSenderPort {
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Value("${app.kafka.topic:words.processed}")
  private String topic;

  @Override
  public void sendResult(ParagraphProcessDto paragraphProcessDto, String key) {
    String json = serializeResult(paragraphProcessDto, key);
    log.info("Sending result to Kafka topic '{}', key='{}': {}", topic, key, json);
    kafkaTemplate
        .send(
            MessageBuilder.withPayload(json)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, key)
                .build())
        .whenComplete(
            (sendResult, throwable) -> {
              if (throwable != null) {
                log.error(
                    "Failed to send message to topic '{}', key='{}': {}",
                    topic,
                    key,
                    throwable.getMessage(),
                    throwable);
              } else {
                log.info("Message sent successfully to topic '{}', key='{}'", topic, key);
              }
            });
  }

  private String serializeResult(ParagraphProcessDto paragraphProcessDto, String key) {
    try {
      return objectMapper.writeValueAsString(paragraphProcessDto);
    } catch (JsonProcessingException e) {
      log.error("Failed to serialize result for key '{}': {}", key, e.getMessage(), e);
      throw new RuntimeException("Failed to serialize result for Kafka", e);
    }
  }
}
