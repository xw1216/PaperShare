package tech.outspace.papershare.repo.objs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentRepo extends JpaRepository<CommentRepo, Long>, JpaSpecificationExecutor<CommentRepo> {
}
