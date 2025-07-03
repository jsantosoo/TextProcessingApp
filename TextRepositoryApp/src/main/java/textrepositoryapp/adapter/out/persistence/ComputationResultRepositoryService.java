package textrepositoryapp.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import textrepositoryapp.domain.ComputationResultDTO;
import textrepositoryapp.adapter.out.mapper.ComputationResultMapper;
import textrepositoryapp.port.ComputationResultPort;

@Service
@RequiredArgsConstructor
public class ComputationResultRepositoryService implements ComputationResultPort {
    private final ComputationResultRepository repository;
    private final ComputationResultMapper computationResultMapper;

    @Value("${result.page-size:10}")
    private int pageSize;

    @Override
    public List<ComputationResultDTO> getLastResults() {
        return computationResultMapper.toDTOList(repository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, pageSize)));
    }
}
