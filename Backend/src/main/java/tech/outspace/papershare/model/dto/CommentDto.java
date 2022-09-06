package tech.outspace.papershare.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.outspace.papershare.model.entity.objs.Comment;
import tech.outspace.papershare.model.entity.objs.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String id;
    private String userName;
    private String cont;
    private LocalDateTime createTime;

    public CommentDto(Comment comment, User user) {
        id = comment.getId();
        userName = user.getName();
        cont = comment.getCont();
        createTime = comment.getCreateTime();
    }
}
