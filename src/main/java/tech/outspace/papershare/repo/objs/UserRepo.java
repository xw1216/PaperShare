package tech.outspace.papershare.repo.objs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import tech.outspace.papershare.model.entity.objs.User;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query("select u from User u where u.email = ?1")
    Optional<User> findByEmail(@NonNull String email);

    @Transactional
    @Modifying
    @Query("update User u set u.signInTime = ?1 where u.id = ?2")
    int updateSignInTimeById(@NonNull LocalDateTime signInTime, @NonNull Long id);


}
