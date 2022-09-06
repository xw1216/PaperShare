package tech.outspace.papershare.repo.rels;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import tech.outspace.papershare.model.entity.rels.RepoPaperRel;

import java.util.List;

public interface RepoPaperRelRepo extends JpaRepository<RepoPaperRel, String> {
    @Query("select r from RepoPaperRel r where r.repoId = ?1")
    List<RepoPaperRel> findByRepoIdEquals(@NonNull String repoId);

    @Query("select (count(r) > 0) from RepoPaperRel r where r.repoId = :repoId and r.paperId = :paperId")
    boolean existsByRepoIdAndPaperId(@Param("repoId") @NonNull String repoId, @Param("paperId") @NonNull String paperId);

    @Transactional
    @Modifying
    @Query("delete from RepoPaperRel r where r.repoId = :repoId and r.paperId = :paperId")
    int deleteByRepoIdAndPaperId(@NonNull String repoId, @NonNull String paperId);

}