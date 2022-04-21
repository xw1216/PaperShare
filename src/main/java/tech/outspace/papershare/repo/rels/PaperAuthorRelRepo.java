package tech.outspace.papershare.repo.rels;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tech.outspace.papershare.model.entity.rels.PaperAuthorRel;

public interface PaperAuthorRelRepo extends JpaRepository<PaperAuthorRel, Long>, JpaSpecificationExecutor<PaperAuthorRel> {
}
