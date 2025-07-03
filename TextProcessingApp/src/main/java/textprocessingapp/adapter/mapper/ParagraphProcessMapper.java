package textprocessingapp.adapter.mapper;

import org.mapstruct.Mapper;
import textprocessingapp.adapter.dto.ParagraphProcessResponseDto;
import textprocessingapp.dto.ParagraphProcessDto;

@Mapper
public interface ParagraphProcessMapper {

  ParagraphProcessResponseDto toResponseDto(ParagraphProcessDto paragraphProcessDto);
}
