package tech.outspace.papershare.repo.objs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import tech.outspace.papershare.model.entity.objs.User;

import java.time.LocalDateTime;

public interface UserRepo extends JpaRepository<User, String> {

    @Query("select (count(u) > 0) from User u where u.email = ?1")
    boolean existsByEmail(@NonNull String email);

    @Query("select u from User u where u.email = ?1")
    User findByEmail(@NonNull String email);

    @Query("select u from User u where u.id = ?1")
    User findByIdEquals(@NonNull String id);

    @Transactional
    @Modifying
    @Query("update User u set u.signInTime = ?1 where u.id = ?2")
    int updateSignInTimeById(@NonNull LocalDateTime signInTime, @NonNull String id);

    @Transactional
    @Modifying
    @Query("update User u set u.name = ?1, u.motto = ?2 where u.id = ?3")
    int updateNameAndMottoByIdEquals(@NonNull String name, @NonNull String motto, @NonNull String id);


}