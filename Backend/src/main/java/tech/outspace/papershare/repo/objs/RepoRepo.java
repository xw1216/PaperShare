package tech.outspace.papershare.repo.objs;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import tech.outspace.papershare.model.entity.objs.Repo;

import java.util.List;

public interface RepoRepo extends JpaRepository<Repo, String> {
    @Query("select r from Repo r where r.userId = ?1")
    List<Repo> findByUserIdEquals(@NonNull String userId);

    @Query("select r from Repo r where r.visible = true order by r.updateTime DESC")
    List<Repo> findByExploreVisible(Pageable pageable);

    @Query("select r from Repo r where r.id = ?1")
    Repo findByIdEquals(@NonNull String id);

    @Transactional
    @Modifying
    @Query("update Repo r set r.name = ?1, r.cont = ?2, r.visible = ?3 where r.id = ?4")
    int updateNameContVisibleByIdEquals(@NonNull String name, @NonNull String cont, @NonNull Boolean visible, @NonNull String id);

    @Transactional
    @Modifying
    @Query("delete from Repo r where r.id = ?1")
    int deleteByIdEquals(@NonNull String id);

    @Query("select (count(r) > 0) from Repo r where r.id = :id")
    boolean existsByIdEquals(@Param("id") @NonNull String id);


}