package tech.outspace.papershare.repo.rels;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tech.outspace.papershare.model.entity.rels.PaperCitedRel;

public interface PaperCitedRelRepo extends JpaRepository<PaperCitedRel, Long>, JpaSpecificationExecutor<PaperCitedRel> {
}
