package textrepositoryapp.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import textrepositoryapp.domain.ComputationResult;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ComputationResultRepository extends MongoRepository<ComputationResult, String> {
    List<ComputationResult> findAllByOrderByCreatedAtDesc(Pageable pageable);
}

