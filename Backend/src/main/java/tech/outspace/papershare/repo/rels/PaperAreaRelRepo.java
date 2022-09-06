package tech.outspace.papershare.repo.rels;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import tech.outspace.papershare.model.entity.rels.PaperAreaRel;

import java.util.List;

public interface PaperAreaRelRepo extends JpaRepository<PaperAreaRel, String> {
    @Query("select p from PaperAreaRel p where p.paperId = ?1")
    List<PaperAreaRel> findByPaperIdEquals(@NonNull String paperId);

    @Query("select count(p) from PaperAreaRel p where p.areaId = ?1")
    long countByAreaIdEquals(@NonNull String areaId);

}