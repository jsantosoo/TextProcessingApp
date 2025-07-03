package textrepositoryapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
@Document(collection = "computationResult")
public class ComputationResult {
    @Id
    private String id;
    private String freqWord;
    private double avgParagraphSize;
    private double totalProcessingTime;
    private Instant createdAt;

}
