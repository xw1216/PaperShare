package tech.outspace.papershare.repo.objs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import tech.outspace.papershare.model.entity.objs.Note;

import java.time.LocalDateTime;
import java.util.List;

public interface NoteRepo extends JpaRepository<Note, String> {
    @Transactional
    @Modifying
    @Query("delete from Note n where n.repoId = :repoId and n.paperId = :paperId")
    int deleteByRepoIdAndPaperId(@NonNull String repoId, @NonNull String paperId);

    @Transactional
    @Modifying
    @Query("delete from Note n where n.id = :id")
    int deleteByIdEquals(@NonNull String id);

    @Query("select n from Note n where n.id = :id")
    Note findByIdEquals(@Param("id") @NonNull String id);

    @Query("select n from Note n " +
            "where n.repoId = :repoId and n.paperId = :paperId and n.page = :page " +
            "order by n.updateTime")
    List<Note> findByRepoPaperAndPage(
            @Param("repoId") @NonNull String repoId,
            @Param("paperId") @NonNull String paperId,
            @Param("page") @NonNull Integer page);

    @Transactional
    @Modifying
    @Query("update Note n set n.title = :title, n.cont = :cont, n.updateTime = :updateTime where n.id = :id")
    int updateTitleAndContAndUpdateTimeById(
            @NonNull @Param("title") String title,
            @NonNull @Param("cont") String cont,
            @NonNull @Param("updateTime") LocalDateTime updateTime,
            @NonNull @Param("id") String id);


}