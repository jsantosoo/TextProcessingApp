package textrepositoryapp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ComputationResultDTO {
    @JsonProperty("freqWord")
    private String freqWord;
    @JsonProperty("avgParagraphSize")
    private double avgParagraphSize;
    @JsonProperty("totalProcessingTime")
    private double totalProcessingTime;
}
