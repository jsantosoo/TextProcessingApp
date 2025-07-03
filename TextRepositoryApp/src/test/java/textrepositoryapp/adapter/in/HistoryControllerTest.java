package textrepositoryapp.adapter.in;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import textrepositoryapp.adapter.in.dto.ComputationResultResponseDto;
import textrepositoryapp.application.HistoryService;
import textrepositoryapp.adapter.out.mapper.ComputationResultMapper;
import textrepositoryapp.domain.ComputationResultDTO;

@ExtendWith(MockitoExtension.class)
class HistoryControllerTest {

    @Mock
    private HistoryService historyService;
    @Mock
    private ComputationResultMapper computationResultMapper;

    private HistoryController historyController;

    @BeforeEach
    void setUp() {
        historyController = new HistoryController(historyService, computationResultMapper);
    }

    @Test
    void getHistory_shouldReturnResults_whenServiceProvidesData() {
        List<ComputationResultDTO> mockDTOs = List.of(mock(ComputationResultDTO.class), mock(ComputationResultDTO.class));
        List<ComputationResultResponseDto> mockResponses = List.of(mock(ComputationResultResponseDto.class), mock(
            ComputationResultResponseDto.class));
        when(historyService.getLast10Results()).thenReturn(mockDTOs);
        when(computationResultMapper.toResponseList(mockDTOs)).thenReturn(mockResponses);
        List<ComputationResultResponseDto> results = historyController.getHistory();
        assertEquals(mockResponses.size(), results.size(), "Expected size of results to match mock responses");
        verify(historyService, times(1)).getLast10Results();
        verify(computationResultMapper, times(1)).toResponseList(mockDTOs);
    }

    @Test
    void getHistory_shouldHandleException_whenServiceThrowsError() {
        when(historyService.getLast10Results()).thenThrow(new RuntimeException("Service error"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> historyController.getHistory());
        assertTrue(exception.getMessage().contains("Failed to fetch last 10 computation results"),
            "Expected exception message to indicate failure in fetching results");
    }
}
