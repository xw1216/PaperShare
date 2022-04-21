package tech.outspace.papershare.repo.objs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NoteRepo extends JpaRepository<NoteRepo, Long>, JpaSpecificationExecutor<NoteRepo> {
}
