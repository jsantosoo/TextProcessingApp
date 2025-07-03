package textrepositoryapp.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import textrepositoryapp.port.ComputationResultPort;
import textrepositoryapp.domain.ComputationResultDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

    @Mock
    private ComputationResultPort computationResultPort;

    private HistoryService historyService;

    @BeforeEach
    void setUp() {
        historyService = new HistoryService(computationResultPort);
    }

    @Test
    void getLast10Results_shouldReturnResults_whenRepositoryProvidesData() {
        List<ComputationResultDTO> mockResults = List.of(mock(ComputationResultDTO.class), mock(ComputationResultDTO.class));
        when(computationResultPort.getLastResults()).thenReturn(mockResults);
        List<ComputationResultDTO> results = historyService.getLast10Results();
        assertEquals(mockResults.size(), results.size(), "Expected size of results to match mock results");
        verify(computationResultPort, times(1)).getLastResults();
    }

    @Test
    void getLast10Results_shouldHandleException_whenRepositoryThrowsError() {
        when(computationResultPort.getLastResults()).thenThrow(new RuntimeException("DB error"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> historyService.getLast10Results());
        assertTrue(exception.getMessage().contains("Could not fetch last computation results"),
            "Expected exception message to indicate failure in fetching results");
    }
}
