package tech.outspace.papershare.repo.objs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tech.outspace.papershare.model.entity.objs.CheckCode;

public interface CheckCodeRepo extends JpaRepository<CheckCode, Long>, JpaSpecificationExecutor<CheckCode> {
}
