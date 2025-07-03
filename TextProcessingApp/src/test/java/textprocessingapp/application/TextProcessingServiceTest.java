package textprocessingapp.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import textprocessingapp.dto.ParagraphProcessDto;
import textprocessingapp.port.ParagraphsFetcherPort;
import textprocessingapp.port.ResultSenderPort;

@ExtendWith(MockitoExtension.class)
class TextProcessingServiceTest {
  private TextProcessingService textProcessingService;

  @Mock private ParagraphsFetcherPort paragraphsFetcherPort;

  @Mock private ResultSenderPort resultSenderPort;

  @BeforeEach
  void setUp() {
    textProcessingService = new TextProcessingService(paragraphsFetcherPort, resultSenderPort);
  }

  @Test
  void processParagraphs_returnsResult() {
    when(paragraphsFetcherPort.fetchParagraph()).thenReturn("test test paragraph");
    // Act
    ParagraphProcessDto result = textProcessingService.processParagraphs(1);
    // Assert
    assertNotNull(result);
    assertEquals("test", result.getFreqWord());
    assertEquals("test test paragraph".length(), result.getAvgParagraphSize(), 0.01);
    verify(resultSenderPort).sendResult(any(), eq("test"));
  }

  @Test
  void processParagraphs_handlesApiErrorGracefully() {
    when(paragraphsFetcherPort.fetchParagraph()).thenThrow(new RuntimeException("API error"));
    ParagraphProcessDto result = textProcessingService.processParagraphs(1);
    assertNotNull(result);
    assertEquals("", result.getFreqWord());
    assertEquals(0, result.getAvgParagraphSize(), 0.01);
    verify(resultSenderPort).sendResult(any(), eq(""));
  }
}
