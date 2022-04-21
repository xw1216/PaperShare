package tech.outspace.papershare.repo.objs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tech.outspace.papershare.model.entity.objs.Note;

public interface NoteRepo extends JpaRepository<Note, Long>, JpaSpecificationExecutor<Note> {
}
