package textprocessingapp.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import textprocessingapp.adapter.mapper.ParagraphProcessMapper;

@Configuration
public class MapperConfig {
  @Bean
  public ParagraphProcessMapper paragraphProcessMapper() {
    return Mappers.getMapper(ParagraphProcessMapper.class);
  }
}
