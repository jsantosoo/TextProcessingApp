package textrepositoryapp.adapter.out.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import textrepositoryapp.domain.ComputationResult;
import textrepositoryapp.domain.ComputationResultDTO;
import textrepositoryapp.adapter.out.mapper.ComputationResultMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComputationResultRepositoryServiceTest {

    @Mock
    private ComputationResultRepository repository;
    @Mock
    private ComputationResultMapper mapper;

    private ComputationResultRepositoryService service;

    @BeforeEach
    void setUp() {
        service = new ComputationResultRepositoryService(repository, mapper);
    }

    @Test
    void getLastResults_shouldReturnMappedDTOs_whenRepositoryReturnsEntities() throws Exception {
        // Ensure pageSize is set to a valid value
        java.lang.reflect.Field pageSizeField = service.getClass().getDeclaredField("pageSize");
        pageSizeField.setAccessible(true);
        pageSizeField.set(service, 10);

        List<ComputationResult> mockEntities = List.of(mock(ComputationResult.class), mock(ComputationResult.class));
        List<textrepositoryapp.domain.ComputationResultDTO> mockDTOs = List.of(mock(ComputationResultDTO.class), mock(ComputationResultDTO.class));
        when(repository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 10))).thenReturn(mockEntities);
        when(mapper.toDTOList(mockEntities)).thenReturn(mockDTOs);
        List<ComputationResultDTO> result = service.getLastResults();
        assertEquals(mockDTOs.size(), result.size(), "Expected size of DTO list to match mock DTOs");
        verify(repository, times(1)).findAllByOrderByCreatedAtDesc(PageRequest.of(0, 10));
        verify(mapper, times(1)).toDTOList(mockEntities);
    }
}
