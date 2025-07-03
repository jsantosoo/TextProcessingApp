package textrepositoryapp.adapter.out.mapper;

import java.time.Instant;
import org.mapstruct.Mapper;
import java.util.List;
import textrepositoryapp.domain.ComputationResult;
import textrepositoryapp.domain.ComputationResultDTO;
import textrepositoryapp.adapter.in.dto.ComputationResultResponseDto;

@Mapper(componentModel = "spring")
public interface ComputationResultMapper {
    ComputationResult toEntity(ComputationResultDTO dto, Instant now);
    List<ComputationResultDTO> toDTOList(List<ComputationResult> results);
    List<ComputationResultResponseDto> toResponseList(List<ComputationResultDTO> dtos);
}
