package tech.outspace.papershare.repo.objs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import tech.outspace.papershare.model.entity.objs.Session;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SessionRepo extends JpaRepository<Session, Long>, JpaSpecificationExecutor<Session> {

    @Transactional
    @Modifying
    @Query("update Session s set s.close = ?1 where s.id = ?2")
    void updateCloseById(@NonNull Boolean close, @NonNull Long id);

    @Transactional
    @Modifying
    @Query("update Session s set s.expireTime = ?1 where s.id = ?2")
    int updateExpireTimeById(@NonNull LocalDateTime expireTime, @NonNull Long id);

    @Transactional
    @Modifying
    @Query("delete from Session s where s.id = ?1")
    void deleteById(@NonNull Long id);

    @Query("select s from Session s where s.userId = ?1")
    Optional<Session> findByUserId(@NonNull Long userId);


}
