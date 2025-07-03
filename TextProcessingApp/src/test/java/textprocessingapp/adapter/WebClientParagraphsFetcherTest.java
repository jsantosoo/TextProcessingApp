package textprocessingapp.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

@ExtendWith(MockitoExtension.class)
class WebClientParagraphsFetcherTest {
  @Mock private WebClient webClient;
  private WebClientParagraphsFetcher fetcher;

  @BeforeEach
  void setUp() {
    fetcher = new WebClientParagraphsFetcher(webClient);
    ReflectionTestUtils.setField(fetcher, "hipsumApiUrl", "http://test-url");
  }

  @Test
  void fetchParagraph_returnsExpectedString() {
    String expected = "Test paragraph";
    WebClient webClientDeep = mock(WebClient.class, RETURNS_DEEP_STUBS);
    fetcher = new WebClientParagraphsFetcher(webClientDeep);
    ReflectionTestUtils.setField(fetcher, "hipsumApiUrl", "http://test-url");

    when(webClientDeep.get().uri("http://test-url").retrieve().bodyToMono(String.class).block())
        .thenReturn(expected);

    String result = fetcher.fetchParagraph();
    assertEquals(expected, result);
  }
}
