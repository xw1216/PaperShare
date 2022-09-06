package tech.outspace.papershare.service.comment;

import org.springframework.stereotype.Service;
import tech.outspace.papershare.model.dto.CommentDto;
import tech.outspace.papershare.model.entity.objs.Comment;
import tech.outspace.papershare.model.entity.objs.User;
import tech.outspace.papershare.model.vo.CommentVo;
import tech.outspace.papershare.repo.objs.CommentRepo;
import tech.outspace.papershare.repo.objs.UserRepo;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentControlService {
    private final CommentRepo commentRepo;
    private final UserRepo userRepo;

    public CommentControlService(CommentRepo commentRepo, UserRepo userRepo) {
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
    }

    public Result<String> addComment(CommentVo vo, String userId) {
        Comment comment = new Comment(vo.getCont(), userId, vo.getPaperId());
        commentRepo.save(comment);
        String str = "发表评论成功";
        return Result.factory(EResult.SUCCESS, str, str);
    }

    public Result<List<CommentDto>> getPaperComments(String paperId) {
        List<Comment> commentList = commentRepo.findByPaperIdTimeDesc(paperId);
        List<CommentDto> dtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            User user = userRepo.findByIdEquals(comment.getUserId());
            dtoList.add(new CommentDto(comment, user));
        }
        return Result.factory(EResult.SUCCESS, "获取文章评论成功", dtoList);
    }

}
