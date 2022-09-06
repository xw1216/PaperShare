package tech.outspace.papershare.repo.rels;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import tech.outspace.papershare.model.entity.rels.UserRepoFocusRel;

import java.util.List;

public interface UserRepoFocusRelRepo extends JpaRepository<UserRepoFocusRel, String> {
    @Query("select u from UserRepoFocusRel u where u.userId = ?1")
    List<UserRepoFocusRel> findByUserIdEquals(@NonNull String userId);

    @Transactional
    @Modifying
    @Query("delete from UserRepoFocusRel u where u.userId = ?1 and u.repoId = ?2")
    int deleteByUserIdAndRepoId(@NonNull String userId, @NonNull String repoId);

}