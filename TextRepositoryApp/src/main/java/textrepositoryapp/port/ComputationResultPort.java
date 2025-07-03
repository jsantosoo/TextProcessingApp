package textrepositoryapp.port;

import java.util.List;
import textrepositoryapp.domain.ComputationResultDTO;

public interface ComputationResultPort {
    List<ComputationResultDTO> getLastResults();
}

