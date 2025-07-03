package textrepositoryapp.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Value("${kafka.topic.words-processed}")
    private String wordsProcessedTopicName;

    @Bean
    public NewTopic wordsProcessedTopic() {
        return new NewTopic(wordsProcessedTopicName, 4, (short) 1);
    }
}
