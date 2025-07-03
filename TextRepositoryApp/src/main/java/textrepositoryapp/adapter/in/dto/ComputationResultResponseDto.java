package textrepositoryapp.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComputationResultResponseDto {
    private String freqWord;
    private double avgParagraphSize;
    private double totalProcessingTime;
}

