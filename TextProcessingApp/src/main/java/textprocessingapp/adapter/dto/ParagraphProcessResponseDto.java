package textprocessingapp.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParagraphProcessResponseDto {
  private String freqWord;
  private double avgParagraphSize;
  private long totalProcessingTime;
}
