package tech.outspace.papershare.repo.objs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import tech.outspace.papershare.model.entity.objs.Session;

import java.time.LocalDateTime;

public interface SessionRepo extends JpaRepository<Session, String> {
    @Transactional
    @Modifying
    @Query("update Session s set s.close = ?1 where s.id = ?2")
    int updateCloseById(@NonNull Boolean close, @NonNull String id);

    @Query("select s from Session s where s.id = ?1")
    Session findByIdEquals(@NonNull String id);


    @Query("select s from Session s where s.userId = ?1")
    Session findByUserId(@NonNull String userId);

    @Transactional
    @Modifying
    @Query("update Session s set s.expireTime = ?1 where s.id = ?2")
    int updateExpireTimeById(@NonNull LocalDateTime expireTime, @NonNull String id);

    @Transactional
    @Modifying
    @Query("delete from Session s where s.id = ?1")
    int deleteByIdEquals(@NonNull String id);


}