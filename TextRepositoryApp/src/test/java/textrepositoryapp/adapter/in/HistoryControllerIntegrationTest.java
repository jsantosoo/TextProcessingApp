package textrepositoryapp.adapter.in;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import textrepositoryapp.application.HistoryService;
import textrepositoryapp.adapter.out.mapper.ComputationResultMapper;
import textrepositoryapp.domain.ComputationResultDTO;
import textrepositoryapp.adapter.in.dto.ComputationResultResponseDto;
import textrepositoryapp.port.ComputationResultPort;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@Import(HistoryControllerIntegrationTest.MockConfig.class)
public class HistoryControllerIntegrationTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ComputationResultPort computationResultPort() {
            return mock(ComputationResultPort.class);
        }
        @Bean
        public HistoryService historyService(ComputationResultPort computationResultPort) {
            return new HistoryService(computationResultPort);
        }
        @Bean
        public ComputationResultMapper computationResultMapper() {
            return mock(ComputationResultMapper.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ComputationResultMapper computationResultMapper;
    @Autowired
    private ComputationResultPort computationResultPort;

    @Test
    public void getHistory_withResults_returnsOkAndJson() throws Exception {
        List<ComputationResultDTO> dtos = List.of(mock(ComputationResultDTO.class));
        List<ComputationResultResponseDto> responses = List.of(mock(ComputationResultResponseDto.class));
        when(computationResultPort.getLastResults()).thenReturn(dtos);
        when(computationResultMapper.toResponseList(dtos)).thenReturn(responses);

        mockMvc.perform(MockMvcRequestBuilders.get("/history"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));

        verify(computationResultPort, times(1)).getLastResults();
    }

    @Test
    public void getHistory_withNoResults_returnsOkAndEmptyJson() throws Exception {
        when(computationResultPort.getLastResults()).thenReturn(List.of());
        when(computationResultMapper.toResponseList(List.of())).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/history"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));

        verify(computationResultPort, atLeastOnce()).getLastResults();
    }
}
