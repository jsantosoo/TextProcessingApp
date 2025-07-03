package textprocessingapp.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
  @Bean
  public NewTopic wordsProcessedTopic() {
    return new NewTopic("words.processed", 4, (short) 1);
  }
}
