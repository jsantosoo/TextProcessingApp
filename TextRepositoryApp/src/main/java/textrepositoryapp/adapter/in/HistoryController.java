package textrepositoryapp.adapter.in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import textrepositoryapp.adapter.in.dto.ComputationResultResponseDto;
import textrepositoryapp.application.HistoryService;
import textrepositoryapp.adapter.out.mapper.ComputationResultMapper;
import textrepositoryapp.domain.ComputationResultDTO;

@Slf4j
@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;
    private final ComputationResultMapper computationResultMapper;

    @GetMapping
    public List<ComputationResultResponseDto> getHistory() {
        log.info("Fetching last 10 computation results from service.");
        try {
            List<ComputationResultDTO> results = historyService.getLast10Results();
            log.info("Successfully fetched {} results.", results.size());
            return computationResultMapper.toResponseList(results);
        } catch (Exception e) {
            log.error("Error fetching last 10 computation results", e);
            throw new RuntimeException("Failed to fetch last 10 computation results", e);
        }
    }
}
