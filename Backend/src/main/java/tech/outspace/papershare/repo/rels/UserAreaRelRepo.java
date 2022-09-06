package tech.outspace.papershare.repo.rels;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import tech.outspace.papershare.model.entity.rels.UserAreaRel;

import java.util.List;

public interface UserAreaRelRepo extends JpaRepository<UserAreaRel, String> {
    @Query("select count(u) from UserAreaRel u where u.areaId = ?1")
    long countByAreaIdEquals(@NonNull String areaId);

    @Query("select u from UserAreaRel u where u.userId = ?1")
    List<UserAreaRel> findByUserIdEquals(@NonNull String userId);

    @Transactional
    @Modifying
    @Query("delete from UserAreaRel u where u.userId = ?1 and u.areaId = ?2")
    int deleteByUserIdAndAreaId(@NonNull String userId, @NonNull String areaId);


}