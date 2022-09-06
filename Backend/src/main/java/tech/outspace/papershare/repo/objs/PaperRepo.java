package tech.outspace.papershare.repo.objs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import tech.outspace.papershare.model.entity.objs.Paper;

import java.util.List;

public interface PaperRepo extends JpaRepository<Paper, String> {
    @Query("select p from Paper p where p.id = ?1")
    Paper findByIdEquals(@NonNull String id);

    @Query("select (count(p) > 0) from Paper p where p.id = :id")
    boolean existsByIdEquals(@Param("id") @NonNull String id);

    @Query("select (count(p) > 0) from Paper p where p.doi = :doi")
    boolean existsByDoiEquals(@Param("doi") @NonNull String doi);

    @Query("select p from Paper p where p.title like :title")
    List<Paper> findByTitleLike(@Param("title") @NonNull String title);

    @Query("select p from Paper p where p.title like concat('%', :title, '%') order by p.year DESC")
    List<Paper> findByTitleContainsOrderByYearDesc(@Param("title") String title);

}