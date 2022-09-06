package tech.outspace.papershare.repo.objs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import tech.outspace.papershare.model.entity.objs.CheckCode;

import java.time.LocalDateTime;

public interface CheckCodeRepo extends JpaRepository<CheckCode, String> {
    @Query("select c from CheckCode c where c.email = ?1")
    CheckCode findByEmail(@NonNull String email);

    @Transactional
    @Modifying
    @Query("delete from CheckCode c where c.id = ?1")
    int deleteByIdEquals(@NonNull String id);

    @Transactional
    @Modifying
    @Query("delete from CheckCode c where c.expireTime > ?1")
    int deleteByExpireTimeGreaterThan(@NonNull LocalDateTime expireTime);

}