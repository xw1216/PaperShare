package tech.outspace.papershare.repo.objs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuthorRepo extends JpaRepository<AuthorRepo, Long>, JpaSpecificationExecutor<AuthorRepo> {
}
