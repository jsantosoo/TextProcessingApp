package textprocessingapp.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import textprocessingapp.port.ParagraphsFetcherPort;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebClientParagraphsFetcher implements ParagraphsFetcherPort {

  private final WebClient webClient;

  @Value("${app.hipsum.api.url}")
  private String hipsumApiUrl;

  @Override
  public String fetchParagraph() {
    try {
      String paragraph =
          webClient.get().uri(hipsumApiUrl).retrieve().bodyToMono(String.class).block();
      return paragraph != null ? paragraph : "";
    } catch (Exception e) {
      log.error("Failed to fetch paragraph", e);
      throw (new RuntimeException("Failed to fetch paragraph from Hipsum API", e));
    }
  }
}
