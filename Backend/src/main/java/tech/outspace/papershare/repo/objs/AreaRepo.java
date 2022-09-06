package tech.outspace.papershare.repo.objs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import tech.outspace.papershare.model.entity.objs.Area;

import java.util.List;

public interface AreaRepo extends JpaRepository<Area, String> {
    @Query("select a from Area a where a.id = ?1")
    Area findByIdEquals(@NonNull String id);

    @Query("select (count(a) > 0) from Area a where a.name = ?1")
    boolean existsByNameEquals(@NonNull String name);

    @Transactional
    @Modifying
    @Query("delete from Area a where a.id = ?1")
    int deleteByIdEquals(@NonNull String id);

    @Query("select a from Area a where a.name is not null")
    List<Area> findAllNotNull();

}