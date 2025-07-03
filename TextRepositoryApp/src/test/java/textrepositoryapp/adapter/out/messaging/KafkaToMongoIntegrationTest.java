// FIXME
// package textrepositoryapp.adapter.out.messaging;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import java.time.Instant;
// import org.apache.kafka.clients.admin.AdminClient;
// import org.apache.kafka.clients.admin.NewTopic;
// import org.apache.kafka.clients.producer.ProducerConfig;
// import org.apache.kafka.common.serialization.StringSerializer;
// import org.awaitility.Awaitility;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.kafka.core.DefaultKafkaProducerFactory;
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.kafka.core.ProducerFactory;
// import org.springframework.test.context.DynamicPropertyRegistry;
// import org.springframework.test.context.DynamicPropertySource;
// import org.testcontainers.containers.KafkaContainer;
// import org.testcontainers.containers.MongoDBContainer;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;
// import textrepositoryapp.adapter.out.persistence.ComputationResultRepository;
// import textrepositoryapp.domain.ComputationResult;
// import textrepositoryapp.domain.ComputationResultDTO;
//
// import java.time.Duration;
// import java.util.Map;
//
// import static org.assertj.core.api.Assertions.assertThat;
//
// @Testcontainers
// @SpringBootTest
// public class KafkaToMongoIntegrationTest {
//
//     @Container
//     static KafkaContainer kafkaContainer = new KafkaContainer("5.5.0");
//
//     @Container
//     static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
//
//     @Autowired
//     private ComputationResultRepository repository;
//
//     private final ObjectMapper objectMapper = new ObjectMapper();
//
//     @AfterEach
//     void cleanUp() {
//         repository.deleteAll();
//     }
//
//     @DynamicPropertySource
//     static void setProperties(DynamicPropertyRegistry registry) {
//         registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
//         registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
//     }
//
//     private KafkaTemplate<String, String> createKafkaTemplate() {
//         Map<String, Object> producerProps = Map.of(
//             "bootstrap.servers", kafkaContainer.getBootstrapServers(),
//             "key.serializer", StringSerializer.class.getName(),
//             "value.serializer", StringSerializer.class.getName()
//         );
//         ProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(producerProps);
//         return new KafkaTemplate<>(pf);
//     }
//
//     @Test
//     void whenKafkaEventConsumed_thenSavedToMongo() throws Exception {
//         // Ensure topic exists
//         try (AdminClient adminClient = AdminClient.create(Map.of("bootstrap.servers", kafkaContainer.getBootstrapServers()))) {
//             adminClient.createTopics(java.util.List.of(new NewTopic("words.processed", 1, (short) 1))).all().get();
//             System.out.println("Topic 'words.processed' created successfully");
//         }
//
//         ComputationResultDTO dto = new ComputationResultDTO();
//         dto.setFreqWord("testword");
//         dto.setAvgParagraphSize(5.0);
//         dto.setTotalProcessingTime(123.45);
//         String json = objectMapper.writeValueAsString(dto);
//
//         KafkaTemplate<String, String> kafkaTemplate = createKafkaTemplate();
//         kafkaTemplate.send("words.processed", "test-key", json).get(); // Ensure send is complete
//         kafkaTemplate.flush();
//         System.out.println("Message sent to Kafka: " + json);
//
//         // Sanity check: Save directly to MongoDB and verify
//         var directResult = textrepositoryapp.domain.ComputationResult.builder()
//             .freqWord("directword")
//             .avgParagraphSize(1.23)
//             .totalProcessingTime(9.87)
//             .createdAt(java.time.Instant.now())
//             .build();
//         repository.save(directResult);
//         var sanityCheck = repository.findAll();
//         System.out.println("Sanity check (should contain directword): " + sanityCheck);
//         assertThat(sanityCheck.stream().anyMatch(r -> "directword".equals(r.getFreqWord()))).isTrue();
//
//         // Wait for consumer to process using Awaitility
//         org.awaitility.Awaitility.await()
//             .atMost(java.time.Duration.ofSeconds(30))
//             .pollInterval(java.time.Duration.ofMillis(500))
//             .untilAsserted(() -> {
//                 var allResults = repository.findAll();
//                 System.out.println("Results in MongoDB: " + allResults);
//                 // Only check for the testword result
//                 assertThat(allResults.stream().anyMatch(r -> "testword".equals(r.getFreqWord()) && r.getAvgParagraphSize() == 5.0 && r.getTotalProcessingTime() == 123.45)).isTrue();
//             });
//     }
//
//     @Test
//     void testMongoConnection() {
//         // Simple test to verify MongoDB connectivity
//         repository.save(new ComputationResult("testword", "testword", 5.0, 123.45, Instant.now()));
//         var results = repository.findAll();
//         assertThat(results).isNotEmpty();
//         System.out.println("MongoDB connection test passed. Results: " + results);
//     }
// }