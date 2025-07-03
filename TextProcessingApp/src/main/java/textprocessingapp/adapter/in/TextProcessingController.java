package textprocessingapp.adapter.in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import textprocessingapp.adapter.dto.ParagraphProcessResponseDto;
import textprocessingapp.adapter.mapper.ParagraphProcessMapper;
import textprocessingapp.application.TextProcessingService;
import textprocessingapp.dto.ParagraphProcessDto;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TextProcessingController {

  private final TextProcessingService textProcessingService;
  private final ParagraphProcessMapper paragraphProcessMapper;

  @PostMapping("/paragraphs/process")
  public ResponseEntity<ParagraphProcessResponseDto> processText(
      @RequestParam(name = "p") int paragraphs) {
    log.info("Received request to process {} paragraphs", paragraphs);
    if (paragraphs <= 0 || paragraphs > 100) {
      log.warn("Invalid parameter 'p': {}. Must be between 1 and 100.", paragraphs);
      return ResponseEntity.badRequest().build();
    }
    try {
      ParagraphProcessDto result = textProcessingService.processParagraphs(paragraphs);
      ParagraphProcessResponseDto response = paragraphProcessMapper.toResponseDto(result);
      log.info("Successfully processed {} paragraphs", paragraphs);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("Exception while processing paragraphs: {}", e.getMessage(), e);
      return ResponseEntity.internalServerError().build();
    }
  }
}
