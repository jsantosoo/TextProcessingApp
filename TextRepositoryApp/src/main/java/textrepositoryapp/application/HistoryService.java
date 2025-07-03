package textrepositoryapp.application;

import lombok.RequiredArgsConstructor;
import textrepositoryapp.domain.ComputationResultDTO;
import org.springframework.stereotype.Service;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import textrepositoryapp.port.ComputationResultPort;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {
    private final ComputationResultPort computationResultPort;

    public List<ComputationResultDTO> getLast10Results() {
        try {
            log.info("Fetching last computation results from repository port.");
            List<ComputationResultDTO> results = computationResultPort.getLastResults();
            log.info("Fetched {} results.", results.size());
            return results;
        } catch (Exception e) {
            log.error("Failed to fetch last computation results", e);
            throw new RuntimeException("Could not fetch last computation results", e);
        }
    }
}

