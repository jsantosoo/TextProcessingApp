package textprocessingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParagraphProcessDto {
  private String freqWord;
  private double avgParagraphSize;
  private long totalProcessingTime;
}
