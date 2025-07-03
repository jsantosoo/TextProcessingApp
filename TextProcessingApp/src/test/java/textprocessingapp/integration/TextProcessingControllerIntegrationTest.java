package textprocessingapp.integration;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import textprocessingapp.adapter.WebClientParagraphsFetcher;
import textprocessingapp.adapter.out.KafkaProducerService;
import textprocessingapp.dto.ParagraphProcessDto;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@Import(TextProcessingControllerIntegrationTest.MockConfig.class)
@Testcontainers
public class TextProcessingControllerIntegrationTest {

  @Container static KafkaContainer kafka = new KafkaContainer();

  @DynamicPropertySource
  static void kafkaProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
  }

  @Autowired private MockMvc mockMvc;

  @TestConfiguration
  static class MockConfig {
    @Bean
    public WebClientParagraphsFetcher webClientParagraphsFetcher() {
      return mock(WebClientParagraphsFetcher.class);
    }

    @Bean
    public KafkaProducerService kafkaProducerService() {
      return mock(KafkaProducerService.class);
    }
  }

  @Autowired private WebClientParagraphsFetcher webClientParagraphsFetcher;

  @Autowired private KafkaProducerService kafkaProducerService;

  @Test
  public void testProcessParagraphs_withValidInput_returnsOk() throws Exception {
    when(webClientParagraphsFetcher.fetchParagraph()).thenReturn("dummy paragraph");
    mockMvc
        .perform(MockMvcRequestBuilders.post("/paragraphs/process").param("p", "5"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.freq_word").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.avg_paragraph_size").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.total_processing_time").exists());
    verify(webClientParagraphsFetcher, times(5)).fetchParagraph();
    verify(kafkaProducerService, atLeastOnce())
        .sendResult(any(ParagraphProcessDto.class), anyString());
  }

  @Test
  public void testProcessParagraphs_withInvalidInput_returnsBadRequest() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/paragraphs/process").param("p", "0"))
        .andExpect(status().isBadRequest());
    mockMvc
        .perform(MockMvcRequestBuilders.post("/paragraphs/process").param("p", "101"))
        .andExpect(status().isBadRequest());
  }
}
