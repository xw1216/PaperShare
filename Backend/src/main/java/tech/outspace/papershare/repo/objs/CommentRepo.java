package tech.outspace.papershare.repo.objs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import tech.outspace.papershare.model.entity.objs.Comment;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, String> {
    @Query("select c from Comment c where c.paperId = :paperId order by c.createTime DESC")
    List<Comment> findByPaperIdTimeDesc(@Param("paperId") @NonNull String paperId);


}