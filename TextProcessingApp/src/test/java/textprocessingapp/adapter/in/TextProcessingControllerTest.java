package textprocessingapp.adapter.in;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import textprocessingapp.adapter.dto.ParagraphProcessResponseDto;
import textprocessingapp.adapter.mapper.ParagraphProcessMapper;
import textprocessingapp.application.TextProcessingService;
import textprocessingapp.dto.ParagraphProcessDto;

@ExtendWith(MockitoExtension.class)
class TextProcessingControllerTest {

  private TextProcessingController textProcessingController;

  @Mock private TextProcessingService textProcessingService;

  @Mock private ParagraphProcessMapper paragraphProcessMapper;

  private static ParagraphProcessDto paragraphProcessDto;
  private static ParagraphProcessResponseDto paragraphProcessResponseDto;

  @BeforeAll
  static void init() {
    paragraphProcessDto = new ParagraphProcessDto("foo", 1.0, 3L);
    paragraphProcessResponseDto =
        ParagraphProcessResponseDto.builder()
            .freqWord("foo")
            .avgParagraphSize(1.0)
            .totalProcessingTime(3L)
            .build();
  }

  @BeforeEach
  void setUp() {
    textProcessingController =
        new TextProcessingController(textProcessingService, paragraphProcessMapper);
  }

  @Test
  void processText_returnsBadRequestForInvalidParam() {
    ResponseEntity<ParagraphProcessResponseDto> response = textProcessingController.processText(0);
    assertEquals(400, response.getStatusCode().value());
    assertNull(response.getBody());
  }

  @Test
  void processText_returnsOkForValidParam() {
    when(textProcessingService.processParagraphs(1)).thenReturn(paragraphProcessDto);
    when(paragraphProcessMapper.toResponseDto(paragraphProcessDto))
        .thenReturn(paragraphProcessResponseDto);

    ResponseEntity<ParagraphProcessResponseDto> response = textProcessingController.processText(1);
    assertEquals(200, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals("foo", response.getBody().getFreqWord());
    assertEquals(1.0, response.getBody().getAvgParagraphSize());
    assertEquals(3L, response.getBody().getTotalProcessingTime());
  }

  @Test
  void processText_returnsServerErrorOnException() {
    when(textProcessingService.processParagraphs(2)).thenThrow(new RuntimeException("fail"));
    ResponseEntity<ParagraphProcessResponseDto> response = textProcessingController.processText(2);
    assertEquals(500, response.getStatusCode().value());
    assertNull(response.getBody());
  }

  @Test
  void processText_returnsBadRequestForParamAboveLimit() {
    ResponseEntity<ParagraphProcessResponseDto> response =
        textProcessingController.processText(101);
    assertEquals(400, response.getStatusCode().value());
    assertNull(response.getBody());
  }
}
