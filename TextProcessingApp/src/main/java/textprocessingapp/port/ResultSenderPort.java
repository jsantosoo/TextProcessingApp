package textprocessingapp.port;

import textprocessingapp.dto.ParagraphProcessDto;

public interface ResultSenderPort {
  void sendResult(ParagraphProcessDto paragraphProcessDto, String key);
}
