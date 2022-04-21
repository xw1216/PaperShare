package tech.outspace.papershare.repo.objs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tech.outspace.papershare.model.entity.objs.Repo;

public interface RepositoryRepo extends JpaRepository<Repo, Long>, JpaSpecificationExecutor<Repo> {
}
