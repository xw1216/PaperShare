package tech.outspace.papershare.control;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tech.outspace.papershare.model.dto.CommentDto;
import tech.outspace.papershare.model.vo.CommentVo;
import tech.outspace.papershare.service.auth.AuthControlService;
import tech.outspace.papershare.service.comment.CommentControlService;
import tech.outspace.papershare.utils.network.HttpFormat;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentControl {

    private final CommentControlService commentService;

    private final AuthControlService authService;

    public CommentControl(CommentControlService commentService, AuthControlService authService) {
        this.commentService = commentService;
        this.authService = authService;
    }

    @PostMapping("/add")
    public Result<String> addComment(@RequestBody CommentVo vo, HttpServletResponse response) {
        try {
            String userId = authService.GetUserIdFromAuth();
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return commentService.addComment(vo, userId);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, e.getMessage());
        }
    }

    @GetMapping("/paper/get")
    public Result<List<CommentDto>> getPaperComments(@RequestParam String id, HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return commentService.getPaperComments(id);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, e.getMessage());
        }
    }

}
